package com.devvikram.varta

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.config.ModelMapper
import com.devvikram.varta.data.firebase.config.Firebase
import com.devvikram.varta.data.firebase.models.conversation.Conversation
import com.devvikram.varta.data.firebase.models.message.ChatMessage
import com.devvikram.varta.data.firebase.repositories.FirebaseConversationRepository
import com.devvikram.varta.data.firebase.repositories.FirebaseMessageRepository
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.data.room.repository.MessageRepository
import com.devvikram.varta.data.room.repository.ParticipantRepository
import com.devvikram.varta.workers.WorkerManagers
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class AppViewModel
@Inject constructor(
    private val firebaseConversationRepository: FirebaseConversationRepository,
    private val firebaseMessageRepository: FirebaseMessageRepository,
    private val conversationRepository: ConversationRepository,
    private val firestore: FirebaseFirestore,
    private val loginPreferences: LoginPreference,
    private val participantRepository: ParticipantRepository,
    private val contactRepository: ContactRepository,
    private val messageRepository: MessageRepository,
    @ApplicationContext private val  context: Context
) : ViewModel() {
    private val logger: Logger = Logger.getLogger(AppViewModel::class.java.name)
    private val conversationCollection =
        firestore.collection(Firebase.FIRESTORE_CONVERSATION_COLLECTION)
    private val messageCollection = firestore.collection(Firebase.FIRESTORE_MESSAGE_COLLECTION)

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val activeConversationIds = mutableSetOf<String>()
    private val messageListeners = mutableMapOf<String, ListenerRegistration>()
    private var conversationListener: ListenerRegistration? = null


    init {
        WorkerManagers.getInstance(context).syncContacts()
        _isUserLoggedIn.value = loginPreferences.getIsLoggedIn()
    }


    fun listenToConversations() {
        println("listening to conversation...")
        conversationCollection
            .whereArrayContains("participantIds", loginPreferences.getUserId())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    logger.warning("Error listening to conversation updates: $error")
                    return@addSnapshotListener
                }
                val newConversationIds = mutableSetOf<String>()
                value?.documents?.forEach { document ->
                    val conversation = document.toObject(Conversation::class.java)

                    if (conversation != null) {
                        viewModelScope.launch {
                            println("FConversation are $conversation")
                            val existingConversation = conversationRepository.getConversationById(conversation.conversationId)
                            if (existingConversation != null) {
                                if (existingConversation.lastModifiedAt < conversation.lastModifiedAt) {
                                    Log.d(TAG, "Updating conversation: $conversation")

                                    // **Update local conversation**

                                    conversationRepository.insertConversation(ModelMapper.mapToRoomConversation(conversation, loginPreferences.getUserId().toInt()))

                                    // **Update local participants**
                                    val localParticipants = participantRepository.getParticipantsByConversationId(conversation.conversationId)
                                    Log.d(TAG, "listenToConversations: $localParticipants")

                                    val localUserIds = localParticipants.map { it.userId }.toSet()

                                    // Step 1: Insert new participants
                                    val newParticipants = conversation.participants.filter { it.userId !in localUserIds }

                                    if (newParticipants.isNotEmpty()) {
                                        Log.d(TAG, "listenToConversations: Inserting new participants...")
                                        participantRepository.insertParticipants(newParticipants.map {
                                            ModelMapper.mapToRoomParticipant(it, conversation.conversationId)
                                        })
                                    }

                                    // Step 2: Check for role changes
                                    Log.d(TAG, "listenToConversations: Checking role updates for existing participants...")

                                    localParticipants.forEach { local ->
                                        val participant = conversation.participants.firstOrNull { it.userId == local.userId }

                                        if (participant == null) {
                                            Log.w(TAG, "Skipping userId=${local.userId} as no matching participant found.")
                                            return@forEach
                                        }

                                        if (local.role != participant.role) {
                                            Log.d(TAG, "Updating participant role for userId=${participant.userId}")
                                            participantRepository.updateParticipantRole(participant.userId, participant.role, conversation.conversationId)
                                        }
                                    }

                                }
                            } else {

                                Log.d(TAG, "listenToConversations: New conversations")
                                // **Insert new conversation**
                                conversationRepository.insertConversation(
                                    ModelMapper.mapToRoomConversation(
                                        conversation,
                                        loginPreferences.getUserId().toInt()
                                    )
                                )

                                // **Insert all participants**
                                participantRepository.insertParticipants(conversation.participants.map {
                                    ModelMapper.mapToRoomParticipant(it, conversation.conversationId)
                                })
                            }
                        }
                        newConversationIds.add(conversation.conversationId)
                    } else {
                        Log.d(TAG, "listenToConversations: null")
                    }
                }
                manageMessageListeners(newConversationIds)
            }
    }
    /** Manage active message listeners based on conversation changes */
    private fun manageMessageListeners(newConversationIds: Set<String>) {
        // Remove listeners for conversations no longer active
        val removedConversations = activeConversationIds - newConversationIds
        removedConversations.forEach { conversationId ->
            messageListeners[conversationId]?.remove()
            messageListeners.remove(conversationId)
            Log.d(TAG, "Stopped listening to messages for conversation: $conversationId")
        }

        // Add listeners for new active conversations
        val addedConversations = newConversationIds - activeConversationIds
        addedConversations.forEach { conversationId ->
            val listener = listenToMessages(conversationId)
            messageListeners[conversationId] = listener
        }

        // Update active conversations
        activeConversationIds.clear()
        activeConversationIds.addAll(newConversationIds)
    }

    private fun listenToMessages(conversationId: String): ListenerRegistration {
        val formater = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = formater.format(Date())
        Log.d(TAG, "Listening to messages for conversation: $conversationId")
        return messageCollection
            .document(conversationId)
            .collection(currentDate)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    logger.warning("Error listening to messages: $error")
                    return@addSnapshotListener
                }
                value?.documents?.forEach { document ->
                    val message = document.toObject(ChatMessage::class.java)
                    if (message != null) {
                        viewModelScope.launch {

                            val existingMessage = messageRepository.getMessageByMessageId(message.messageId)
                            if (existingMessage != null) {
                                // update existing message
                                val existingConversation = conversationRepository.getConversationById(existingMessage.conversationId)
                                val participants = existingConversation.participantIds
                                participants.forEach { _ ->
                                    messageRepository.insertMessage(ModelMapper.mapToRoomMessage(message))
                                }
                            }else{
                                // insert new message
                                messageRepository.insertMessage(ModelMapper.mapToRoomMessage(message))
                            }
                        }
                    }
                }
            }
    }
    override fun onCleared() {
        super.onCleared()
        conversationListener?.remove()
        messageListeners.values.forEach { it.remove() }
        messageListeners.clear()
    }

    fun updateLoginStatus(status: Boolean) {

        println("updateLoginStatus called with status: $status")
        if(!status){
            viewModelScope.launch {
                loginPreferences.clearSharedPref()
                contactRepository.deleteContacts()
                conversationRepository.deleteConversations()
                participantRepository.deleteParticipants()
                messageRepository.deleteMessages()
            }
        }
        _isUserLoggedIn.postValue(status)
    }

    fun createAndGetFirebaseToken() {
        val savedToken = loginPreferences.getFirebaseToken()
        Log.d(TAG, "createAndGetFirebaseToken: $savedToken")

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    println("Fetching FCM token failed: ${task.exception}")
                    return@addOnCompleteListener
                }
                val newToken = task.result

                if (savedToken.isNullOrEmpty() || savedToken != newToken) {
                    println("New FCM Token: $newToken")
                    loginPreferences.setFirebaseToken(newToken)
                } else {
                    println("FCM Token is up-to-date, no need to update.")
                }
            }
    }

}