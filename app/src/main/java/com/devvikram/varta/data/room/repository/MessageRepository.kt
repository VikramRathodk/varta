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


import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.devvikram.varta.data.firebase.models.conversation.LastMessage
import com.devvikram.varta.utility.AppUtils

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map


@Singleton
class MessageRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val messageDao: MessageDao,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    @ApplicationContext private val context: Context
) {
    suspend fun insertNewMessage(roomMessage: RoomMessage, newMessageId: (String) -> Unit = { "" }) {
        // unique message id
        val messageId = firebaseFirestore.collection(Firebase.FIRESTORE_MESSAGE_COLLECTION).document().id
        val newMessage = roomMessage.copy(messageId = messageId)
        messageDao.insertMessage(newMessage)
        newMessageId(messageId)
        Log.d(TAG, "insertNewMessage: Created new message with id: $newMessage")
        // Add another debug log after insertion
        val savedMessage = messageDao.getMessageByMessageId(messageId)
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

    suspend fun updateReceivedField(
        messageId: String,
        updatedReceivedBy: Map<String, Long>,
    ) {
        messageDao.updateMessageReceivedByMap(messageId, updatedReceivedBy.toMap())

    }
    suspend fun updateReadField(
        messageId: String,
        updatedReadBy: Map<String, Long>,){
        messageDao.updateMessageReadBy(messageId, updatedReadBy.toMap())
    }

    suspend fun updateLastModifiedAt(messageId: String, currentTime: Long) {
        messageDao.updateLastModifiedAt(messageId, currentTime)

    }
    fun getPagedMessages(conversationId: String): Flow<PagingData<RoomMessage>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { messageDao.getPagedMessages(conversationId) }
        ).flow
    }


    suspend fun updateMessage(roomMessage: RoomMessage) {
        messageDao.updateMessage(roomMessage)
    }


    suspend fun deleteMessageById(messageId: String) {
        messageDao.deleteMessageById(messageId)
    }


    fun getLastMessageFlow(conversationId: String): Flow<LastMessage> {
        return messageDao.getLastMessageFlow(conversationId)
            .map { messageEntity ->
                messageEntity?.let { ModelMapper.mapToLastMessage(it) }
                    ?: LastMessage(
                        timestamp = 0L,
                        mediaUrl = null,
                        mediaType = null,
                        messageId = "",
                        senderId = "",
                        senderName = "",
                        messageType = AppUtils.getMessageType(""),
                        text = "",
                        isReadBy = emptyMap(),
                        isReceivedBy = emptyMap()
                    )
            }
    }


    //query Method
    fun getUnreadMessagesCountByConversationId(conversationId: String, userId: String): Flow<Int> {
        return messageDao.getUnreadMessagesCountByConversationId(conversationId, userId)
    }

    suspend fun getUnReadMessages(conversationId: String, userId: String): List<RoomMessage> {
        return messageDao.getUnReadMessages(conversationId, userId)
    }

    //filtering Method
//    fun getUnreadMessagesCountByConversationId(conversationId: String, userId: String): Flow<Int> {
//        val allMessageFroConversation = messageDao.getAllMessagesByConversationId(conversationId)
//        return allMessageFroConversation.map { messages ->
//            messages.count { message ->
//                val isReadByMap = message.isReadBy
//                val senderId = message.senderId
//                !isReadByMap.containsKey(userId) && senderId != userId
//            }
//        }
//    }
}