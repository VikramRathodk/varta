package com.devvikram.varta.data.room.dao

import androidx.room.*
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.ui.itemmodels.ParticipantWithContact
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {
    @Upsert
    suspend fun insertParticipant(participant: RoomParticipant)

    @Upsert
    suspend fun insertParticipants(participants: List<RoomParticipant>)

    @Query("SELECT * FROM participants WHERE userId = :userId")
    fun findParticipantByUserId(userId: String): RoomParticipant

    @Query("SELECT * FROM participants WHERE conversationId = :conversationId")
    suspend fun getParticipantsByConversationId(conversationId: String): List<RoomParticipant>

    @Query("DELETE FROM participants")
    suspend fun deleteAllParticipants()

    @Query("SELECT * FROM participants WHERE conversationId = :conversationId")
     fun getParticipantsByConversationIdWithFlow(conversationId: String):  Flow<List<RoomParticipant>>

     @Query("SELECT * FROM participants WHERE conversationId = :conversationId")
     fun getParticipantsWithConversationIdAndUser(conversationId: String): Flow<List<ParticipantWithContact>>

     @Query("UPDATE participants SET role = :role WHERE userId = :userId AND conversationId = :conversationId")
     suspend fun updateParticipantRole(userId: String, role: String, conversationId: String)

    @Query("DELETE FROM participants WHERE userId = :userId AND conversationId = :conversationId")
    suspend fun deleteParticipant(userId: String, conversationId: String): Int

}