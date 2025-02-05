package com.devvikram.varta.data.room.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.devvikram.varta.data.config.ModelMapper
import com.devvikram.varta.data.firebase.models.conversation.Participant
import com.devvikram.varta.data.firebase.repositories.FirebaseConversationRepository
import com.devvikram.varta.data.room.dao.ParticipantDao
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.ui.itemmodels.ParticipantWithContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParticipantRepository @Inject constructor(private val participantDao: ParticipantDao,
                                                 private val firebaseRepository: FirebaseConversationRepository) {
    private val updateMutex = Mutex()
    fun insertParticipants(participants: List<RoomParticipant>) {
        CoroutineScope(Dispatchers.IO).launch {
            participantDao.insertParticipants(participants)
        }
    }

    fun insertNewParticipants(
        conversationId: String,
        roomParticipant: List<RoomParticipant>,
        selectedParticipants: List<Participant>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "insertNewParticipants conversationId : $conversationId")
                val existingParticipants = participantDao.getParticipantsByConversationId(conversationId)
                val newParticipants = roomParticipant.filter { newParticipant ->
                    existingParticipants.none { it.userId == newParticipant.userId }
                }

                if (newParticipants.isNotEmpty()) {
                    participantDao.insertParticipants(newParticipants)
                }

                firebaseRepository.addParticipantsToConversation(conversationId, selectedParticipants)

                try {
                    val conversationUpdateFields = mapOf("lastModifiedAt" to System.currentTimeMillis())
                    firebaseRepository.updateConversation(conversationId, conversationUpdateFields)
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating conversation timestamp: ${e.message}", e)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in insertParticipants: ${e.message}", e)
            }
        }
    }

    suspend fun getParticipantsByConversationId(conversationId: String): List<RoomParticipant> {
        return participantDao.getParticipantsByConversationId(conversationId)
    }

    fun deleteParticipants() {
        CoroutineScope(Dispatchers.IO).launch {
            participantDao.deleteAllParticipants()
        }
    }

    fun getParticipantsByConversationIdWithFlow(conversationId: String): Flow<List<RoomParticipant>> {
        return participantDao.getParticipantsByConversationIdWithFlow(conversationId)
    }

    fun getParticipantsWithConversationId(conversationId: String ): Flow<List<ParticipantWithContact>> {
        return participantDao.getParticipantsWithConversationIdAndUser(conversationId)
    }

    suspend fun updateRole(participant: RoomParticipant) {
        Log.d(TAG, "updateRole: $participant")
            participantDao.updateParticipantRole(participant.userId, participant.role.toString(), conversationId = participant.conversationId)
            try {
                firebaseRepository.updateParticipantRole(conversationId = participant.conversationId,ModelMapper.mapToParticipant(participant))
            } catch (e: Exception) {
                Log.e(TAG, "Error updating participant role: ${e.message}", e)
            }
    }

    suspend fun deleteParticipant(conversationId: String, userId: String) {
        Log.d(TAG, "deleteParticipant: ")
        val deletedRows = participantDao.deleteParticipant(userId, conversationId)
        Log.d("TAG", "Rows deleted: $deletedRows")
        try {
                firebaseRepository.removeParticipantFromConversation(conversationId, userId)
            } catch (e: Exception) {
                Log.e(TAG, "Error removing participant from conversation: ${e.message}", e)

        }
    }

    fun insertParticipant(mapToRoomParticipant: RoomParticipant) {
        CoroutineScope(Dispatchers.IO).launch {
            participantDao.insertParticipant(mapToRoomParticipant)
        }

    }

    suspend fun updateParticipantRole(userId: String, role: String, conversationId: String) {
        updateMutex.withLock {
            Log.d(TAG, "updateParticipantRole: $userId , $role , $conversationId")
            try {
                participantDao.updateParticipantRole(
                    userId = userId,
                    role = role,
                    conversationId = conversationId
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error updating participant role: ${e.message}", e)
            }
        }
    }
}