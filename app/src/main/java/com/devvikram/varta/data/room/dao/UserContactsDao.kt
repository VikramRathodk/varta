package com.devvikram.varta.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.devvikram.varta.data.room.models.ProContacts
import kotlinx.coroutines.flow.Flow

@Dao
interface UserContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetUserContact(proContacts: ProContacts)

    @Upsert
    suspend fun updateContact(proContacts: ProContacts)

    @Upsert
    suspend fun updateAllUsersContacts(updatedContacts: List<ProContacts>)

    @Query("SELECT * FROM pro_contacts WHERE user_id = :proUserId")
    suspend fun getContactByUserId(proUserId: String): ProContacts

    @Query("DELETE FROM pro_contacts WHERE user_id = :proUserId")
    suspend fun deleteSingleUser(proUserId: Long)

    @Query("SELECT * FROM pro_contacts")
     fun getAllUsersContacts(): Flow<List<ProContacts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsersContacts(proContacts: List<ProContacts>)

    @Query("SELECT * FROM pro_contacts WHERE name = :name")
    suspend fun getContactByUsernameNormal(name: String): ProContacts

    @Query("DELETE FROM pro_contacts")
    suspend fun clearContact()

    @Query("SELECT * FROM pro_contacts WHERE user_id = :proUserId")
    fun getContactByUserIdFlow(proUserId: String): Flow<ProContacts>


    @Query("SELECT * FROM pro_contacts WHERE user_id IN (:participantsIds)")
    fun getParticipantsByIds(participantsIds: List<String>): Flow<List<ProContacts>>

    @Query("DELETE FROM pro_contacts")
    suspend fun deleteContacts()


}