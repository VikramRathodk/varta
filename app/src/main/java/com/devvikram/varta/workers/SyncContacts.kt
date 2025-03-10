package com.devvikram.varta.workers

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.config.ModelMapper
import com.devvikram.varta.data.firebase.models.FContact
import com.devvikram.varta.data.room.repository.ContactRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

@HiltWorker
class SyncContacts @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted val contactRepository: ContactRepository,
    private val loginPreference: LoginPreference,
    private val firebaseFirestore: FirebaseFirestore
) : CoroutineWorker(appContext, workerParams) {

    private val userCollection = firebaseFirestore.collection(com.devvikram.varta.data.firebase.config.Firebase.FIREBASE_USER_COLLECTION)

    override suspend fun doWork(): Result {
        val currentUserId = loginPreference.getUserId()
        println("doWork: Syncing contacts $currentUserId")

        return try {
            println("doWork: Syncing contacts")

            syncContacts(currentUserId)

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "doWork: Error occurred while syncing contacts: ${e.message}")
            Result.retry()
        }
    }

    private fun syncContacts(currentUserId: String) {

        userCollection.addSnapshotListener{
            snapshot, e ->

            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                runBlocking {
                    snapshot.documents.forEach { doc ->

                         val fContact = doc.toObject(FContact::class.java)
                        Log.d(TAG, "syncContacts: firebase contacts: $fContact")

                        if(fContact != null){

                            val bitmap =
                                com.devvikram.varta.utility.AppUtils.downloadImage(fContact.profilePic)
                            val localProfilePicPath =
                                bitmap?.let {
                                    com.devvikram.varta.utility.AppUtils.saveImageToStorage(
                                        it,
                                        fContact.name
                                    )
                                }

                            val proContact =
                                ModelMapper.mapToFContact(fContact, localProfilePicPath.toString())
                            val existingContact = contactRepository.getContactByUsernameNormal(proContact.name)
                            if (existingContact == null) {
                                contactRepository.insertUserContact(proContact)
                            } else {
                                contactRepository.updateContact(existingContact)
                            }
                        }

                    }
 
                }
            }

        }

    }


}

