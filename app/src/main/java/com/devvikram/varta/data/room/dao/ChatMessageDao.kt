package com.devvikram.varta.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devvikram.varta.data.room.models.RoomMessage

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: RoomMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<RoomMessage>)

    @Query("SELECT * FROM messages  WHERE conversationId = :conversationId ORDER BY timestamp")
    suspend fun getMessagesForConversation(conversationId: String): List<RoomMessage>

    @Query("SELECT * FROM messages WHERE messageId = :messageId")
    suspend fun getMessage(messageId: String): RoomMessage?

    @Delete
    suspend fun deleteMessage(message: RoomMessage)
}
