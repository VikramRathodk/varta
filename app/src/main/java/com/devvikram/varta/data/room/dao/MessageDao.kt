package com.devvikram.varta.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.devvikram.varta.data.room.models.RoomMessage
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {

    @Upsert
    suspend fun insertMessage(message: RoomMessage)

    @Upsert
    suspend fun insertMessages(messages: List<RoomMessage>)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId Order By timestamp ASC")
    fun getAllMessagesByConversationId(conversationId: String): Flow<List<RoomMessage>>

    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<RoomMessage>>

    @Query("SELECT * FROM messages")
    suspend fun getAllMessagesAsList(): List<RoomMessage>


    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()


    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessageByMessageId(messageId: String): RoomMessage

    @Query("UPDATE messages SET isReceivedBy = :updatedReceivedBy WHERE messageId = :messageId")
    suspend fun updateMessageReceivedByMap(messageId: String, updatedReceivedBy: Map<String, Long>)

    @Query("UPDATE messages SET isReadBy = :updatedReadBy WHERE messageId = :messageId")
    suspend fun updateMessageReadBy(messageId: String, updatedReadBy: Map<String, Long>)


    @Query("UPDATE messages SET lastModifiedAt = :currentTime WHERE messageId = :messageId")
    suspend fun updateLastModifiedAt(messageId: String, currentTime: Long)


    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC")
    fun getPagedMessages(conversationId: String): PagingSource<Int, RoomMessage>


    @Upsert
    suspend fun updateMessage(roomMessage: RoomMessage)

    @Query("DELETE FROM messages WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessageFromConversationId(conversationId: String) :RoomMessage

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT 1")
    fun getLastMessageFlow(conversationId: String): Flow<RoomMessage?>


    @Query("""
    SELECT COUNT(*) FROM messages 
    WHERE conversationId = :conversationId 
    AND senderId != :userId 
    AND (isReadBy IS NULL OR isReadBy NOT LIKE '%"' || :userId || '":%')
""")
    fun getUnreadMessagesCountByConversationId(conversationId: String, userId: String): Flow<Int>


    @Query("""
    SELECT * FROM messages 
    WHERE conversationId = :conversationId
    AND senderId != :userId
    AND (isReadBy IS NULL OR isReadBy NOT LIKE '%"' || :userId || '":%')
""")
    suspend fun getUnReadMessages(conversationId: String, userId: String): List<RoomMessage>

}