package com.devvikram.varta.data.firebase.repositories

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import com.devvikram.varta.data.firebase.models.message.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
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

//    // Update message metadata (e.g., reactions, read status)
//    suspend fun updateMessageField(
//        conversationId: String,
//        date: String,
//        messageId: String,
//        fieldName: String,
//        value: Any
//    ) {
//        try {
//            getMessagesCollection(conversationId, date)
//                .document(messageId)
//                .update(fieldName, value)
//                .await()
//        } catch (e: Exception) {
//            logger.warning("Error updating message field: $e")
//        }
//    }


}