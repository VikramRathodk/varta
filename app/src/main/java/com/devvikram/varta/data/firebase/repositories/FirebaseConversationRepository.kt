package com.devvikram.varta.data.firebase.repositories


import android.content.ContentValues.TAG
import android.util.Log
import com.devvikram.varta.data.firebase.models.conversation.Conversation
import com.devvikram.varta.data.firebase.models.conversation.LastMessage
import com.devvikram.varta.data.firebase.models.conversation.Participant
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.logging.Logger
import javax.inject.Inject

class FirebaseConversationRepository
@Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val logger = Logger.getLogger(FirebaseConversationRepository::class.java.name)

    //function to create a new conversation
    suspend fun createConversation(conversation: Conversation) {
        try {
            firestore.collection("conversations")
                .document(conversation.conversationId)
                .set(conversation)
                .await()
        } catch (e: Exception) {
            logger.warning("Error creating conversation: $e")
        }
    }

    //update a conversation in firestore by fields
    suspend fun updateConversation(conversationId: String, fields: Map<String, Any>) {
        try {
            firestore.collection("conversations")
                .document(conversationId)
                .update(fields)
                .await()
        } catch (e: Exception) {
            logger.warning("Error updating conversation: $e")
        }
    }

    //add participants to a conversation
    suspend fun addParticipantsToConversation(
        conversationId: String,
        participants: List<Participant>
    ) {
        Log.d(TAG, "addParticipantsToConversation: $participants")

        try {
            val participantsMapList = participants.map { participant ->
                mapOf(
                    "userId" to participant.userId,
                    "role" to participant.role
                )
            }

            firestore.collection("conversations")
                .document(conversationId)
                .update(
                    "participants", FieldValue.arrayUnion(*participantsMapList.toTypedArray()),
                    "participantIds", FieldValue.arrayUnion(*participants.map { it.userId }.toTypedArray())
                )
                .await()

            Log.d(TAG, "Participants successfully added.")

        } catch (e: Exception) {
            Log.e(TAG, "Error adding participants to conversation: ${e.message}", e)
        }
    }


    //remove participants from a conversation
    suspend fun removeParticipantsFromConversation(
        conversationId: String,
        participants: List<Participant>
    ) {
        try {
            firestore.collection("conversations")
                .document(conversationId)
                .update(
                    "participants", FieldValue.arrayRemove(participants),
                    "participantIds", FieldValue.arrayRemove(participants.map { it.userId })
                )
                .await()
        } catch (e: Exception) {
            logger.warning("Error removing participants from conversation: $e")
        }
    }

    //add banned user to a conversation
    suspend fun addBannedUserToConversation(
        conversationId: String,
        userId: String
    ) {
        try {
            firestore.collection("conversations")
                .document(conversationId)
                .update(
                    "bannedUser.$userId", true
                )
                .await()
        } catch (e: Exception) {
            logger.warning("Error adding banned user to conversation: $e")
        }
    }

    //remove banned user from a conversation
    suspend fun removeBannedUserFromConversation(
        conversationId: String,
        userId: String
    ) {
        try {
            firestore.collection("conversations")
                .document(conversationId)
                .update(
                    "bannedUser.$userId", FieldValue.delete()
                )
                .await()
        } catch (e: Exception) {
            logger.warning("Error removing banned user from conversation: $e")
        }
    }

    // update conversation last message
    suspend fun updateConversationLastMessage(
        conversationId: String,
        lastMessage: Map<String, LastMessage>
    ) {
        try {
            firestore.collection("conversations")
                .document(conversationId)
                .update(
                    "lastMessage", lastMessage
                )
                .await()
        } catch (e: Exception) {
            logger.warning("Error updating conversation last message: $e")
        }
    }

    suspend fun updateParticipantRole(conversationId: String, participant: Participant) {
        try {
            val conversationRef = firestore.collection("conversations").document(conversationId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(conversationRef)
                val participantsList = snapshot.get("participants") as? MutableList<Map<String, Any>> ?: mutableListOf()

                val index = participantsList.indexOfFirst { it["userId"] == participant.userId }
                if (index != -1) {
                    val updatedParticipant = participantsList[index].toMutableMap()
                    updatedParticipant["role"] = participant.role
                    participantsList[index] = updatedParticipant
                }

                transaction.update(conversationRef, mapOf(
                    "participants" to participantsList,
                    "lastModifiedAt" to System.currentTimeMillis()
                ))
            }.await()

            Log.d(TAG, "Participant role updated successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating participant role: ${e.message}", e)
        }
    }


    suspend fun removeParticipantFromConversation(conversationId: String, userId: String) {
        try {
            val conversationRef = firestore.collection("conversations").document(conversationId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(conversationRef)

                val participantsList = (snapshot.get("participants") as? List<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

                Log.d(TAG, "Participants before removal: $participantsList")

               val removed = participantsList.removeAll { it["userId"] == userId }

                Log.d(TAG, "Participants after removal: $participantsList, Removed: $removed")

                if (removed) {
                    transaction.update(conversationRef, mapOf(
                        "participants" to participantsList,
                        "participantIds" to participantsList.mapNotNull { it["userId"]?.toString() },
                        "lastModifiedAt" to System.currentTimeMillis()
                    ))
                    Log.d(TAG, "Successfully removed participant: $userId")
                } else {
                    Log.w(TAG, "User $userId not found in conversation: $conversationId")
                }
            }.await()

        } catch (e: Exception) {
            Log.e(TAG, "Error removing participant from conversation: ${e.message}", e)
        }
    }


}
