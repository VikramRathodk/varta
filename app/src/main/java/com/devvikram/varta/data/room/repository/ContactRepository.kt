package com.devvikram.varta.data.room.repository

import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.dao.UserContactsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val userContactsDao: UserContactsDao
) {

    suspend fun insertUserContact(userContact: ProContacts) {
        userContactsDao.insetUserContact(userContact)
    }

    suspend fun getContactByUserId(proUserId: String) : ProContacts {
       return userContactsDao.getContactByUserId(
           proUserId = proUserId.toLong()
       )
    }

    fun getContactByUserIdFlow(proUserId: String) : Flow<ProContacts> {
        return userContactsDao.getContactByUserIdFlow(
            proUserId = proUserId.toLong()
        )
    }
     fun getAllUsersContacts() : Flow<List<ProContacts> >{
        return userContactsDao.getAllUsersContacts()
    }


   suspend fun clearCurrentRecord(proUserId: String) {
       userContactsDao.deleteSingleUser(
           proUserId = proUserId.toLong()
       )
   }

    suspend fun saveContacts(proContacts: List<ProContacts>) {
        userContactsDao.insertAllUsersContacts(proContacts)
    }

    suspend fun getContactByUsernameNormal(name: String): ProContacts {
        return userContactsDao.getContactByUsernameNormal(name)

    }

    suspend fun updateUserContacts(updatedContacts: List<ProContacts>) {
        userContactsDao.updateAllUsersContacts(updatedContacts)
    }

    suspend fun clearContact() {
        return userContactsDao.clearContact()

    }

     fun getParticipantsByIds(participantsIds: List<String>): Flow<List<ProContacts>> {
        return userContactsDao.getParticipantsByIds(participantsIds)

    }

    fun addParticipants() {


    }

    suspend fun deleteContacts() {
        userContactsDao.deleteContacts()
    }

}