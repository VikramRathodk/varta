package com.devvikram.varta.data.room.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.devvikram.varta.data.config.ModelMapper
import com.devvikram.varta.data.firebase.config.Firebase
import com.devvikram.varta.data.firebase.repositories.FirebaseMessageRepository
import com.devvikram.varta.data.room.dao.MessageDao
import com.devvikram.varta.data.room.models.RoomConversation
import com.devvikram.varta.data.room.models.RoomMessage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val messageDao: MessageDao,
    private val firebaseMessageRepository: FirebaseMessageRepository,
) {
    suspend fun insertNewMessage(roomMessage: RoomMessage,existingConversation : RoomConversation) {
        // unique message id
        val messageId = firebaseFirestore.collection(Firebase.FIRESTORE_MESSAGE_COLLECTION).document().id
        val newMessage = roomMessage.copy(messageId = messageId)
        messageDao.insertMessage(newMessage)
        Log.d(TAG, "insertNewMessage: Created new message with id: $newMessage")

        firebaseMessageRepository.sendMessage(ModelMapper.mapToChatMessage(newMessage))
    }
    suspend fun insertMessage(roomMessage: RoomMessage){
        messageDao.insertMessage(roomMessage)
    }

    suspend fun insertMessages(messages: List<RoomMessage>){
        messageDao.insertMessages(messages)
    }

     fun getAllMessagesByConversationId(conversationId: String): Flow<List<RoomMessage>> {
         val messages = messageDao.getAllMessagesByConversationId(conversationId)
        return messages
    }

    suspend fun deleteMessages() {
        messageDao.deleteAllMessages()
    }

    fun getAllMessages(): Flow<List<RoomMessage>> {
        return messageDao.getAllMessages()
    }

    suspend fun getAllMessagesAsList(): List<RoomMessage> {
        return messageDao.getAllMessagesAsList()
    }

    suspend fun getMessageByMessageId(messageId: String): RoomMessage {
        return messageDao.getMessageByMessageId(messageId)
    }
}