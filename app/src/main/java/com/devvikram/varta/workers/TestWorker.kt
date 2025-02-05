package com.devvikram.varta.workers

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.retrofit.RetrofitInstance
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.utility.AppUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

@HiltWorker
class TestWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted val contactRepository: ContactRepository,
    private val loginPreference: LoginPreference
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val currentUserId = loginPreference.getUserId()
        println("doWork: Syncing contacts $currentUserId")

        return try {
            println("doWork: Syncing contacts")
            runBlocking {
                val currentContact = contactRepository.getContactByUserId(currentUserId)
                if (currentContact != null) {
                    if(currentContact.profilePic.isEmpty()){
                        if(currentContact.localProfilePicPath.isEmpty()){
                            val bitmap = AppUtils.downloadImage(currentContact.profilePic)
                            val localProfilePicPath = AppUtils.saveImageToStorage(bitmap, currentContact.name)
                            contactRepository.insertUserContact(currentContact.copy(
                                localProfilePicPath = localProfilePicPath
                            ))
                        }
                    }
                    val (internalContacts, externalContacts) = fetchContactsInParallel(
                        currentUserId.toInt())
                    val combinedContacts = (internalContacts + externalContacts)
                    processContacts(combinedContacts)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "doWork: Error occurred while syncing contacts: ${e.message}")
            Result.retry()
        }
    }

    private suspend fun fetchContactsInParallel(userId: Int): Pair<List<ProContacts>, List<ProContacts>> {
        return coroutineScope {
            val internalContactsDeferred = async {
                try {
                    val response = RetrofitInstance.getApiService()
                        .getInternalContacts(userId, page = 1, limit = 5)
                    if (response.isSuccessful) response.body()!!.data else emptyList()
                } catch (e: Exception) {
                    Log.e("FetchContacts", "Failed to fetch internal contacts: ${e.message}")
                    emptyList<ProContacts>()
                }
            }

            val externalContactsDeferred = async {
                try {
                    val response = RetrofitInstance.getApiService()
                        .getExternalContacts(userId, page = 1, limit = 5)
                    if (response.isSuccessful) response.body()!!.data else emptyList()
                } catch (e: Exception) {
                    Log.e("FetchContacts", "Failed to fetch external contacts: ${e.message}")
                    emptyList<ProContacts>()
                }
            }

            Pair(internalContactsDeferred.await(), externalContactsDeferred.await())
        }
    }

    private suspend fun processContacts(contacts: List<ProContacts>) {
        Log.d(TAG, "processContacts: ${contacts.size}")
        var count = 0
        contacts.forEach { proContact ->
            Log.d(TAG, "processContacts: $proContact")
            try {
                if (!isValidUrlFormat(proContact.profilePic)) {
                    Log.d(TAG, "processContacts: Invalid URL Format for ${proContact.name}. Skipping this contact.")
                    return@forEach
                }
                val existingContact = contactRepository.getContactByUsernameNormal(proContact.name)
                if (existingContact == null) {
                    contactRepository.insertUserContact(proContact)
                } else {
                    if (existingContact.profilePic != proContact.profilePic) {

                        val bitmap = AppUtils.downloadImage(proContact.profilePic)
                        val localProfilePicPath = AppUtils.saveImageToStorage(bitmap, proContact.name)
                        contactRepository.insertUserContact(existingContact.copy(
                            profilePic = proContact.profilePic,
                            localProfilePicPath = localProfilePicPath
                        ))
                    } else if (
                        existingContact.localProfilePicPath.isEmpty()
                    ) {
                        val bitmap = AppUtils.downloadImage(existingContact.profilePic)
                        val localProfilePicPath = AppUtils.saveImageToStorage(bitmap, proContact.name)
                        contactRepository.insertUserContact(existingContact.copy(
                            localProfilePicPath = localProfilePicPath
                        ))
                    }
                }
            }catch (e: Exception) {
                Log.e(TAG, "Error processing contact: ${e.message}")
            }

            Log.d(TAG, "processContacts: $count")
            count++
        }
    }

    private fun isValidUrlFormat(url: String): Boolean {
        return try {
            if (url.isNullOrEmpty()) {
                return false
            }
            val sanitizedUrl = url.replace(Regex("(?<=https?://)/*"), "/")
            val uri = Uri.parse(sanitizedUrl)
            uri != null && uri.scheme?.startsWith("http") == true
        } catch (e: Exception) {
            Log.e(TAG, "Error sanitizing URL: ${e.message}")
            false
        }
    }
    }

