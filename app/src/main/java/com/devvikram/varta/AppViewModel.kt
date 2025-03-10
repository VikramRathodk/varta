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
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.data.room.repository.MessageRepository
import com.devvikram.varta.data.room.repository.ParticipantRepository
import com.devvikram.varta.utility.NotificationHelper
import com.devvikram.varta.workers.WorkerManagers

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.installations.FirebaseInstallations
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    private val notificationHelper: NotificationHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val logger: Logger = Logger.getLogger(AppViewModel::class.java.name)

    private val conversationCollection = firestore.collection(Firebase.FIRESTORE_CONVERSATION_COLLECTION)

    private val _currentConversationId = MutableLiveData<String>("")
    val currentConversationId: LiveData<String> get() = _currentConversationId

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _deviceToken = MutableStateFlow<String>("")
    val deviceToken: StateFlow<String> get() = _deviceToken.asStateFlow()

    private val _deviceName = MutableStateFlow<String>("")
    val deviceName: StateFlow<String> get() = _deviceName.asStateFlow()

    private val _deviceOsName = MutableStateFlow("")
    val deviceOsName: StateFlow<String> get() = _deviceOsName.asStateFlow()

    private val _deviceOsVersion = MutableStateFlow<String>("")
    val deviceOsVersion: StateFlow<String> get() = _deviceOsVersion.asStateFlow()

    private val activeConversations = mutableSetOf<String>()
    private val messageListeners = mutableMapOf<String, ListenerRegistration>()

    private val _deviceUniqueId = MutableStateFlow<String>("")
    val deviceUniqueId = _deviceUniqueId.asStateFlow()


    init {
        _deviceName.value = "${android.os.Build.BRAND} - ${android.os.Build.MODEL}"
        _deviceOsName.value = "ANDROID"
        _deviceOsVersion.value = android.os.Build.VERSION.RELEASE

        firebaseConfig()
        loginPreferences.setAppOpenState(true)
        notificationHelper.createNotificationChannels()
        WorkerManagers.getInstance(context).apply {
            syncContacts()
//            syncConversationsAndMessages()
        }
        _isUserLoggedIn.value = loginPreferences.getIsLoggedIn()
        _currentConversationId.observeForever {
            if (it!= null) {
                if (it.isNotEmpty() || it.isNotBlank()) {
                    syncMessagesForActiveConversations(it)
                } else {
                    activeConversations.clear()
                    messageListeners.clear()
                }
            }
        }
    }

    fun setCurrentConversation(conversationId: String) {
        Log.d(TAG, "Setting current conversation: $conversationId")
        _currentConversationId.value = conversationId
        loginPreferences.setCurrentlyActiveConversation(conversationId)
    }


    private fun isConversationOpen(conversationId: String): Boolean {
        Log.d(TAG, "isConversationOpen: {${conversationId == _currentConversationId.value}")
        return conversationId == _currentConversationId.value
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

                value?.documents?.forEach { document ->
                    val conversation = document.toObject(Conversation::class.java)

                    if (conversation != null) {
                        viewModelScope.launch {

                            println("FConversation are $conversation")
                            val existingConversation = conversationRepository.getConversationById(conversation.conversationId)
                            if (existingConversation != null) {
                                if (existingConversation.lastModifiedAt < conversation.lastModifiedAt) {
                                    updateParticipants(conversation)
                                    updateLocalConversation(conversation)
                                    syncMessagesForActiveConversations(conversationId = conversation.conversationId)
                                }
                            }
                            else {

                                Log.d(TAG, "listenToConversations: New conversations")

                                Log.d(TAG, "listenToConversations: $conversation")
                                // **Insert new conversation**
                                conversationRepository.insertConversation(
                                    ModelMapper.mapToRoomConversation(
                                        conversation,
                                        loginPreferences.getUserId()
                                    )
                                )

                                participantRepository.insertParticipants(conversation.participants.map {
                                    ModelMapper.mapToRoomParticipant(
                                        it,
                                        conversation.conversationId
                                    )
                                })
                                if (conversation.type == "P") {
                                    storeConversationIdLocally(conversation)
                                }
                                syncMessagesForActiveConversations(conversation.conversationId)
                            }
                        }
                    } else {
                        Log.d(TAG, "listenToConversations: null")
                    }
                }
            }
    }

    private fun storeConversationIdLocally(conversation: Conversation) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "storeConversationIdLocally: Started for conversationId: ${conversation.conversationId}")

                val loggedInUserId = loginPreferences.getUserId()
                val receiverId = conversation.participantIds.firstOrNull { it != loggedInUserId }?.toLong()
                    ?: return@launch

                contactRepository.getContactByUserIdFlow(receiverId.toString())
                    .filterNotNull()
                    .first()
                    .let { contact ->
                        Log.d(TAG, "storeConversationIdLocally: Contact found. Updating conversationId...")
                        contactRepository.insertUserContact(contact.copy(conversationId = conversation.conversationId))
                        Log.d(TAG, "storeConversationIdLocally: Contact successfully updated!")
                    }

            } catch (e: Exception) {
                Log.e(TAG, "storeConversationIdLocally: Error occurred", e)
            }
        }
    }

    private suspend fun updateParticipants(conversation: Conversation) {
        val localParticipants = participantRepository.getParticipantsByConversationId(conversation.conversationId)
        val remoteUserIds = conversation.participants.map { it.userId }.toSet()
        val localUserIds = localParticipants.map { it.userId }.toSet()

        removeOldParticipants(localParticipants, remoteUserIds, conversation.conversationId)
        insertNewParticipants(conversation, localUserIds)
        updateExistingParticipants(conversation, localParticipants)
    }

    private fun removeOldParticipants(localParticipants: List<RoomParticipant>, remoteUserIds: Set<String>, conversationId: String) {
        val participantsToRemove = localParticipants.filter { it.userId !in remoteUserIds }
        if (participantsToRemove.isNotEmpty()) {
            Log.d(TAG, "Removing participants: ${participantsToRemove.map { it.userId }}")
            participantRepository.deleteParticipantsByUserIds(participantsToRemove.map { it.userId.toInt() }, conversationId)
        }
    }

    private fun insertNewParticipants(conversation: Conversation, localUserIds: Set<String>) {
        val newParticipants = conversation.participants.filter { it.userId !in localUserIds }
        if (newParticipants.isNotEmpty()) {
            participantRepository.insertParticipants(newParticipants.map {
                ModelMapper.mapToRoomParticipant(it, conversation.conversationId)
            })
        }
    }

    private suspend fun updateExistingParticipants(conversation: Conversation, localParticipants: List<RoomParticipant>) {
        localParticipants.forEach { local ->
            val participant = conversation.participants.firstOrNull { it.userId.toInt() == local.userId.toInt() }

            if (participant == null) {
                Log.w(TAG, "Skipping userId=${local.userId} as no matching participant found.")
                return@forEach
            }

            if (local.role != participant.role) {
                Log.d(TAG, "Updating participant role for userId=${participant.userId}")
                participantRepository.updateParticipantRole(
                    participant.userId,
                    participant.role,
                    conversation.conversationId
                )
            }
        }
    }

    private suspend fun updateLocalConversation(conversation: Conversation) {
        conversationRepository.insertConversation(
            ModelMapper.mapToRoomConversation(
                conversation,
                loginPreferences.getUserId()
            )
        )
    }

    private fun syncMessagesForActiveConversations(conversationId: String) {
        Log.d(TAG, "syncMessagesForActiveConversations: $conversationId")
        if (!activeConversations.contains(conversationId)) {
            activeConversations.add(conversationId)
        }
        syncMessages(conversationId)
    }



    private fun syncMessages(conversationId: String) {
        Log.d(TAG, "syncMessages: Syncing Messages for conversation $conversationId")
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = formatter.format(Date())
        val userId = loginPreferences.getUserId()

        // Remove existing listener before adding a new one
        messageListeners[conversationId]?.remove()

        messageListeners[conversationId] =
            firestore.collection(Firebase.FIRESTORE_MESSAGE_COLLECTION)
                .document(conversationId)
                .collection(currentDate)
                .orderBy("lastModifiedAt", Query.Direction.DESCENDING)
                // Remove limit to get all messages
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.e(TAG, "Error listening to messages: ${error.message}")
                        return@addSnapshotListener
                    }

                    snapshots?.documentChanges?.forEach { change ->
                        val message = change.document.toObject(ChatMessage::class.java)
                        Log.d(TAG, "Message sync event: ${change.type} for message ${message.messageId}")

                        viewModelScope.launch {
                            handleMessageSync(message, userId, change.type)
                        }
                    }
                }
    }

    private suspend fun handleMessageSync(
        firebaseMessage: ChatMessage,
        userId: String,
        changeType: DocumentChange.Type
    ) {
        Log.d(TAG, "handleMessageSync: ${firebaseMessage.messageId} - Type: $changeType")
        val currentTime = System.currentTimeMillis()

        when (changeType) {
            DocumentChange.Type.ADDED -> {
                handleAddedMessage(firebaseMessage, userId, currentTime)
            }
            DocumentChange.Type.MODIFIED -> {
                Log.d(TAG, "Message Modified: ${firebaseMessage.messageId}")
                updateLocalDatabase(firebaseMessage)
            }
            DocumentChange.Type.REMOVED -> {
                Log.d(TAG, "Message Removed: ${firebaseMessage.messageId}")
                deleteMessageFromLocalDatabase(firebaseMessage.messageId)
            }
        }
    }

    private suspend fun handleAddedMessage(
        firebaseMessage: ChatMessage,
        userId: String,
        currentTime: Long
    ) {
        Log.d(TAG, "handleAddedMessage: Adding ${firebaseMessage.messageId}")

        val readBy = firebaseMessage.isReadBy?.toMutableMap() ?: mutableMapOf()
        val receivedBy = firebaseMessage.isReceivedBy?.toMutableMap() ?: mutableMapOf()

        var isUpdated = false

        if (isConversationOpen(firebaseMessage.conversationId)) {
            if (!readBy.containsKey(userId)) {
                readBy[userId] = currentTime
                isUpdated = true
            }
        } else {
            if (!receivedBy.containsKey(userId)) {
                receivedBy[userId] = currentTime
                isUpdated = true
            }
        }

        if (isUpdated) {
            val updatedMessage = firebaseMessage.copy(
                isReadBy = readBy,
                isReceivedBy = receivedBy,
                lastModifiedAt = currentTime
            )

            messageRepository.updateReadField(messageId = updatedMessage.messageId,updatedMessage.isReadBy)
            messageRepository.updateReceivedField(messageId = updatedMessage.messageId,updatedMessage.isReceivedBy)
            messageRepository.updateLastModifiedAt(messageId = updatedMessage.messageId,currentTime)
            firebaseMessageRepository.updateMessageInFirebase(updatedMessage)
        }
    }
    private suspend fun updateLocalDatabase(message: ChatMessage) {
        val existingMessage = messageRepository.getMessageByMessageId(message.messageId)

        if (existingMessage != null) {
            val updatedMessage = ModelMapper.mapToRoomMessage(
                chatMessage = message,
                existingMessage = existingMessage
            )

            if (existingMessage.mediaStatus != "UPLOADING") {
                messageRepository.insertMessage(updatedMessage)
            }
        } else {
            messageRepository.insertMessage(ModelMapper.mapToRoomMessage(message))
        }
    }

    private fun deleteMessageFromLocalDatabase(messageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.deleteMessageById(messageId)
        }
    }

    private fun removeConversationListener(conversationId: String) {
        activeConversations.remove(conversationId)
        messageListeners[conversationId]?.remove()
        messageListeners.remove(conversationId)
    }

    fun updateLoginStatus(status: Boolean) {
        if (!status) {
            viewModelScope.launch {
                loginPreferences.clearSharedPref()
                contactRepository.deleteContacts()
                conversationRepository.deleteConversations()
                participantRepository.deleteParticipants()
                messageRepository.deleteMessages()
                _isUserLoggedIn.postValue(false)
            }
        } else {
            _isUserLoggedIn.postValue(true)
        }
    }

    fun markAsReadMessage(conversationId: String) {

    }

    suspend fun startMessageSync(conversationId: String) {
        if(conversationId.isNotBlank() or conversationId.isNotEmpty()){
            if (!activeConversations.contains(conversationId)) {
                activeConversations.add(conversationId)
            }
            syncMessagesForActiveConversations(conversationId)
        }
    }

    private fun firebaseConfig() {
        FirebaseInstallations.getInstance().id.addOnCompleteListener { installationTask ->
            if (installationTask.isSuccessful) {
                _deviceUniqueId.value = installationTask.result
                Log.d("Firebase", "Device Unique ID: ${installationTask.result}")
                loginPreferences.setDeviceUniqueId(_deviceUniqueId.value)
            } else {
                Log.w("Firebase", "Failed to get Firebase Installation ID", installationTask.exception)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        loginPreferences.setAppOpenState(false)
        removeConversationListener(activeConversations.firstOrNull() ?: "")
    }

}