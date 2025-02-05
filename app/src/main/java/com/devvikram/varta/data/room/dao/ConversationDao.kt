package com.devvikram.varta.data.room.dao

import androidx.room.*
import com.devvikram.varta.data.room.models.ConversationWithContact
import com.devvikram.varta.data.room.models.RoomConversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Upsert
    suspend fun insertConversation(conversation: RoomConversation): Long

    @Upsert
    suspend fun insertConversations(conversations: List<RoomConversation>): List<Long>

    @Query("SELECT * FROM conversations WHERE conversationId = :conversationId")
    suspend fun getConversationById(conversationId: String): RoomConversation

    @Query("SELECT * FROM conversations")
    suspend fun getAllConversations(): List<RoomConversation>


    @Transaction
    @Query("SELECT * FROM conversations")
    fun getAllConversationWithContact(): Flow<List<ConversationWithContact>>

    @Query("UPDATE conversations SET name = :groupName, description = :groupDescription WHERE conversationId = :conversationId")
    suspend fun updateConversation(conversationId: String, groupName: String, groupDescription: String)

    @Query("SELECT participantIds FROM conversations WHERE conversationId = :conversationId")
    suspend fun getParticipantsByConversationId(conversationId: String): List<String>

    @Query("DELETE FROM conversations")
    suspend fun deleteAllConversations()

}