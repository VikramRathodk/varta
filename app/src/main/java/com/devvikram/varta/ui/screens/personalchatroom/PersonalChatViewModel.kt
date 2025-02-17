package com.devvikram.varta.ui.screens.personalchatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.firebase.models.conversation.Participant
import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.models.RoomConversation
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.data.room.repository.MessageRepository
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val contactRepository: ContactRepository,
    private val conversationRepository: ConversationRepository,
    private val loginPreference: LoginPreference,

) : ViewModel() {

    private val LOG_TAG = PersonalChatViewModel::class.java.simpleName
    private val _conversationId = MutableStateFlow("")
    val conversationId: StateFlow<String> = _conversationId.asStateFlow()

    private val _messagesList = MutableStateFlow<Set<PersonalChatMessageItem>>(emptySet())
    val messagesList: StateFlow<Set<PersonalChatMessageItem>> = _messagesList.asStateFlow()

    private val _selectedMessageItem = MutableStateFlow<PersonalChatMessageItem?>(null)
    val selectedMessageItem: StateFlow<PersonalChatMessageItem?> get() = _selectedMessageItem

    // user profile
    private val _userProfile = MutableStateFlow<ProContacts?>(null)
    val userProfile: StateFlow<ProContacts?> get() = _userProfile


    private val _receiverUserProfile = MutableStateFlow<ProContacts?>(null)
    val receiverUserProfile: StateFlow<ProContacts?> get() = _receiverUserProfile


    private suspend fun createPersonalChatMessageList(currentConversationid: String) {
        Log.d(TAG, "createPersonalChatMessageList: current conversation Id : ${_conversationId.value}")

         messageRepository.getAllMessagesByConversationId(currentConversationid).collect{
             messages->
             messages.forEach {
                     message->
                 Log.d(TAG, "createPersonalChatMessageList: Current Message with conversationId : ${message.conversationId} ---> $message")
                 if(message.senderId == loginPreference.getUserId()){
                     val messageItem = PersonalChatRoomUtil.createSenderMessage(message)
                     _messagesList.value += messageItem
                 }else{
                     val messageItem = PersonalChatRoomUtil.createReceiverMessage(message)
                     _messagesList.value += messageItem
                 }
             }
         }

    }

    fun sendMessage(message: String){
        println("personal: sendMessage: $message")
        val senderUserId = loginPreference.getUserId()
        viewModelScope.launch {
            val existingConversation =
                conversationRepository.getConversationById(_conversationId.value)
            val receiverUserId = _receiverUserProfile.value?.userId.toString()
            val participantIds = listOf(senderUserId, receiverUserId)

            if(existingConversation != null){
                // Step 1 : create a new message with message type text
                Log.d(
                    LOG_TAG,
                    "sendMessage: existingConversation: ${existingConversation.conversationId} name : ${existingConversation.name}"
                )
                val newMessage = RoomMessage(
                    messageId = "",
                    conversationId = existingConversation.conversationId,
                    text = message,
                    senderId = senderUserId,
                    messageType = MessageType.TEXT.toString(),
                    timestamp = System.currentTimeMillis(),
                )
                Log.d(TAG, "sendMessage: Creating Existing message Without Message Id :$newMessage")

                messageRepository.insertNewMessage(newMessage,existingConversation)
            }else{

                val newMessage = RoomMessage(
                    messageId = "",
                    conversationId = "",
                    text = message,
                    senderId = senderUserId,
                    messageType = MessageType.TEXT.toString(),
                    timestamp = System.currentTimeMillis(),
                )
                Log.d(TAG, "sendMessage: Creating New message Without Message Id :$newMessage")

                val conversation = RoomConversation(
                    conversationId = "",
                    userId = receiverUserId,
                    type = "P",
                    createdBy = senderUserId,
                    createdAt = System.currentTimeMillis(),
                    participantIds = participantIds
                )

                val roomParticipants = participantIds.map {
                    RoomParticipant(
                        localParticipantId = 0,
                        userId = it,
                        conversationId = conversation.conversationId,
                        role = "MEMBER"
                    )
                }
                val participants = participantIds.map { Participant(userId = it, role = "MEMBER") }

                println("personal: conversation not exits creating new conversation $conversation")
                conversationRepository.createConversationWithMessage(conversation, roomParticipants, participants, newMessage)
            }

        }
    }

    fun forwardMessage(selectedChatMessageList: List<PersonalChatMessageItem>) {
        println("personal: forwardMessage: $selectedChatMessageList")
    }

    fun updateSelectedMessageItem(item: PersonalChatMessageItem?) {
        _selectedMessageItem.value = item
    }

    fun getReceiverInfo(receiverId: String) {
        viewModelScope.launch {
            println("personal: getCurrentReceiver called ${receiverId}")
            val data = contactRepository.getContactByUserId(receiverId)
            _receiverUserProfile.value = data
        }
    }

    fun updateConversationId(currentConversationid: String) {
        _conversationId.value = currentConversationid
        viewModelScope.launch {
            createPersonalChatMessageList(currentConversationid)
        }
    }
}