package com.devvikram.varta.ui.screens.groupchatroom.chatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.data.room.repository.MessageRepository
import com.devvikram.varta.ui.screens.groupchatroom.GroupChatRoomUtils
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupChatRoomViewmodel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val contactRepository: ContactRepository,
    private val loginPreference: LoginPreference,
    private val conversationRepository: ConversationRepository,
) : ViewModel() {

    private val _currentConversationId = MutableStateFlow("")
    val currentConversationId: StateFlow<String> = _currentConversationId.asStateFlow()

    private val _selectedMessageItem = MutableStateFlow<GroupChatMessageItem?>(null)
    val selectedMessageItem: StateFlow<GroupChatMessageItem?> = _selectedMessageItem.asStateFlow()

    private val _messagesList = MutableStateFlow<Set<GroupChatMessageItem>>(emptySet())
    val messagesList: StateFlow<Set<GroupChatMessageItem>> = _messagesList.asStateFlow()


    private suspend fun createGroupChatMessageList(conversationId: String) {
        messageRepository.getAllMessagesByConversationId(conversationId).collect{
            messages->
            messages.forEach {
                message ->
                if(message.senderId == loginPreference.getUserId()){
                    val messageItem = GroupChatRoomUtils.createSenderMessageForGroup(message)
                    _messagesList.value += messageItem
                }else{
                    val messageItem = GroupChatRoomUtils.createReceiverMessageForGroup(message)
                    _messagesList.value += messageItem
                }
            }
        }
    }

    fun forwardMessage(selectedChatItemList: List<GroupChatMessageItem>) {
        println("forwardMessage: in Group chat $selectedChatItemList")
    }
    fun setSelectedChatMessageItem(selectedChatMessageItem: GroupChatMessageItem?) {
        viewModelScope.launch {
            _selectedMessageItem.value = selectedChatMessageItem
            Log.d(TAG, "ViewModel - Selected message updated: ${_selectedMessageItem.value}")
        }
    }

    fun sendMessage(message: String) {
        println("sendMessage: in Group chat $message")
        viewModelScope.launch {
            val existingConversation = conversationRepository.getConversationById(_currentConversationId.value)

            if(existingConversation != null){
                val newMessage = RoomMessage(
                    messageId = "",
                    conversationId = existingConversation.conversationId,
                    text = message,
                    senderId = loginPreference.getUserId(),
                    messageType = MessageType.TEXT.toString(),
                    timestamp = System.currentTimeMillis(),
                )
                messageRepository.insertNewMessage(newMessage,existingConversation)
            }
        }
    }

    fun updateConversationId(conversationId: String) {
        _currentConversationId.value = conversationId
        Log.d(TAG, "ViewModel - Current conversation updated: $conversationId")
        viewModelScope.launch {
            createGroupChatMessageList(conversationId)
        }
    }



}