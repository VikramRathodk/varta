package com.devvikram.varta.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.devvikram.varta.data.room.models.RoomMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Upsert
    suspend fun insertMessage(message: RoomMessage)

    @Upsert
    suspend fun insertMessages(messages: List<RoomMessage>)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId")
    fun getAllMessagesByConversationId(conversationId: String): Flow<List<RoomMessage>>

    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<RoomMessage>>

    @Query("SELECT * FROM messages")
   suspend fun getAllMessagesAsList(): List<RoomMessage>


    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()


    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessageByMessageId(messageId: String): RoomMessage
}