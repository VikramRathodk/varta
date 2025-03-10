package com.devvikram.varta.data.firebase.repositories

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.devvikram.varta.data.firebase.config.Firebase
import com.devvikram.varta.data.firebase.models.message.ChatMessage
import com.devvikram.varta.utility.AppUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.Logger
import javax.inject.Inject




class FirebaseMessageRepository @Inject constructor(private val firestore: FirebaseFirestore,private val firebaseConversationRepository: FirebaseConversationRepository) {
    private val logger = Logger.getLogger(FirebaseMessageRepository::class.java.name)

    // Function to get messages collection for a specific date
    private fun getMessagesCollection(conversationId: String, date: String) =
        firestore
            .collection("all_messages")
            .document(conversationId)
            .collection(date)


    // Send a message
    @SuppressLint("SimpleDateFormat")
    suspend fun sendMessage(chatMessage: ChatMessage) {
        Log.d(TAG, "sendMessage: $chatMessage")
        try {
            val formater = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = formater.format(Date())
            Log.d(TAG, "sendMessage Firebase Current time: $currentDate")

            getMessagesCollection(chatMessage.conversationId, currentDate)
                .document(chatMessage.messageId)
                .set(chatMessage)
                .await()

            firebaseConversationRepository.updateConversation(chatMessage.conversationId,
                mapOf("lastModifiedAt" to System.currentTimeMillis()))
        } catch (
            e: Exception
        ) {
            logger.warning("Error sending message: $e")
        }
    }

    suspend fun updateMessageField(conversationId: String,
                                   messageId: String,
                                   field: Map<String, Any>) {
        try {
            val formater = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = formater.format(Date())
            Log.d(TAG, "sendMessage Firebase Current time: $currentDate")
            getMessagesCollection(conversationId, currentDate)
                .document(messageId)
                .update(field)
                .await()
        } catch (
            e: Exception
        ) {
            logger.warning("Error sending message: $e")
        }
    }

    suspend fun updateMessageInFirebase(message: ChatMessage) {
        try {
            firestore.collection(Firebase.FIRESTORE_MESSAGE_COLLECTION)
                .document(message.conversationId)
                .collection(AppUtils.getFormattedDate())
                .document(message.messageId)
                .set(message, SetOptions.merge())
                .await()

            Log.d(TAG, "Message updated in Firestore successfully: ${message.messageId}")
        } catch (error: Exception) {
            Log.e(TAG, "Failed to update message in Firestore: ${error.message}")
        }
    }

    fun updateFirebaseMessageField(conversationId: String, timeStamp: Long, messageId: String, isReadBy: Map<String, Long>) {
        val messageRef = firestore.collection(Firebase.FIRESTORE_MESSAGE_COLLECTION)
            .document(conversationId)
            .collection(AppUtils.getDateFromTimestamp(timeStamp))
            .document(messageId)

        messageRef.update("isReadBy", isReadBy)
            .addOnSuccessListener {
                Log.d(TAG, "updateFirebaseMessageField: Successfully updated message $messageId")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "updateFirebaseMessageField: Failed to update message $messageId. Error: ${e.message}")
            }
    }

}