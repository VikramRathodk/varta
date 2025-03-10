package com.devvikram.varta.data.room.repository

import com.devvikram.varta.data.config.ModelMapper
import com.devvikram.varta.data.firebase.config.Firebase
import com.devvikram.varta.data.firebase.models.conversation.Participant
import com.devvikram.varta.data.firebase.repositories.FirebaseConversationRepository
import com.devvikram.varta.data.room.dao.ConversationDao
import com.devvikram.varta.data.room.models.ConversationWithContact
import com.devvikram.varta.data.room.models.RoomConversation
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.data.room.models.RoomParticipant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository
@Inject constructor(
    private val conversationDao: ConversationDao,
    private val firestore: FirebaseFirestore,
    private val firebaseConversationRepository: FirebaseConversationRepository,
    private val participantRepository: ParticipantRepository,
    private val messageRepository: MessageRepository
) {
    suspend fun createConversationWithMessage(
        conversation: RoomConversation,
        roomParticipants: List<RoomParticipant>,
        participants: List<Participant>,
        newMessage: RoomMessage,
        updateConversationId :(String) -> Unit
    ) {
        val conversationId = firestore.collection(Firebase.FIRESTORE_CONVERSATION_COLLECTION).document().id
        updateConversationId(conversationId)

        val newConversation = conversation.copy(conversationId = conversationId)

        val newParticipants = roomParticipants.map { it.copy(conversationId = conversationId) }

        conversationDao.insertConversation(newConversation)

        messageRepository.insertNewMessage(newMessage.copy(conversationId = conversationId))
        participantRepository.insertNewParticipants(conversationId,newParticipants,participants)
        firebaseConversationRepository.createConversation(ModelMapper.mapToConversation(newConversation,participants))
    }

    suspend fun createConversation(
        conversation: RoomConversation,
        roomParticipants: List<RoomParticipant>,
        participants: List<Participant>,
    ) {
        val conversationId = firestore.collection(Firebase.FIRESTORE_CONVERSATION_COLLECTION).document().id

        val newConversation = conversation.copy(conversationId = conversationId)

        val newParticipants = roomParticipants.map { it.copy(conversationId = conversationId) }

        conversationDao.insertConversation(newConversation)
        participantRepository.insertNewParticipants(conversationId,newParticipants,participants)
        firebaseConversationRepository.createConversation(ModelMapper.mapToConversation(newConversation,participants))
    }


    suspend fun insertConversation(conversation: RoomConversation) {
        conversationDao.insertConversation(conversation)
    }

    suspend fun getConversationById(conversationId: String): RoomConversation {
        return conversationDao.getConversationById(conversationId)
    }

    fun getAllConversationWithContact(): Flow<List<ConversationWithContact>> {
        return conversationDao.getAllConversationWithContact()
    }

    suspend fun updateConversation(
        conversationId: String,
        fields: Map<String, Any>
    ) {
        val groupName = fields["groupName"]
        val groupDescription = fields["groupDescription"]
        conversationDao.updateConversation(conversationId,groupName.toString(),groupDescription.toString())
        firebaseConversationRepository.updateConversation(conversationId, fields)

    }

    suspend fun getParticipantsByConversationId(value: String): List<String> {
        return conversationDao.getParticipantsByConversationId(value)

    }

    suspend fun deleteConversations() {
        conversationDao.deleteAllConversations()
    }

    suspend fun updateConversationLastModifiedTimeStamp(converationId: String) {
        val lastTimeStampMap = HashMap<String,Any>()
        lastTimeStampMap["lastModifiedAt"] = System.currentTimeMillis()
        firebaseConversationRepository.updateConversation(conversationId = converationId, fields = lastTimeStampMap)
    }

    fun getConversationByIdFlow(value: String): Flow<RoomConversation> {
        return conversationDao.getConversationByIdFlow(value)

    }

    suspend fun getConversationAndContactWithConversationUniqueKey(conversationId: String): ConversationWithContact {
        return conversationDao.getConversationAndContactWithConversationConversationId(conversationId)

    }

}