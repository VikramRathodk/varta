package com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.data.room.repository.ParticipantRepository
import com.devvikram.varta.ui.itemmodels.ConversationItem
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupMemberItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainGroupChatRoomViewModel @Inject constructor(
    private val participantRepository: ParticipantRepository,
    private val contactRepository: ContactRepository,
    private val loginPreference: LoginPreference,
    private val conversationRepository: ConversationRepository
):ViewModel(){

    private val LOG_TAG = MainGroupChatRoomViewModel::class.java.simpleName

    private val _currentConversationId = MutableStateFlow("")
    val currentConversationId: StateFlow<String> = _currentConversationId.asStateFlow()

    private val _groupInformation = MutableStateFlow<ConversationItem.GroupConversation?>(null)
    val groupInformation: StateFlow<ConversationItem.GroupConversation?> = _groupInformation.asStateFlow()

    private val _groupMembers = MutableStateFlow<ArrayList<GroupMemberItem>>(ArrayList())
    val groupMembers: StateFlow<ArrayList<GroupMemberItem>> = _groupMembers.asStateFlow()

    private val originalList = ArrayList<GroupMemberItem>()
    init {
        viewModelScope.launch {
            currentConversationId.collect { id ->
                if (id.isNotEmpty()) {
                    loadGroupInformation(id)
                }
            }
        }

    }
    fun setCurrentConversationId(conversationId: String) {
        _currentConversationId.value = conversationId
    }

    private fun loadGroupInformation(currentConversationId: String) {
        viewModelScope.launch {
            val conversation = conversationRepository.getConversationById(currentConversationId)
            println("Group information Conversation $conversation")
            _groupInformation.value = conversation.let {
                ConversationItem.GroupConversation(
                    conversationId = conversation.conversationId,
                    groupDescription = conversation.description.toString(),
                    groupIconUrl = "",
                    groupIconLocalPath ="",
                    lastMessage = "",
                    lastMessageTime = "",
                    lastMessageSender = 1,
                    groupName = conversation.name.toString(),
                    participants = conversation.participantIds,
                    createdBy = conversation.createdBy

                )
            }
            getParticipants()
        }
    }
    private fun getParticipants() {
        viewModelScope.launch {
            participantRepository.getParticipantsWithConversationId(_currentConversationId.value)
                .collect { participantList ->

                    val updatedMembersMap = mutableMapOf<String, GroupMemberItem>()

                    for (participantWithContact in participantList) {
                        val contact = participantWithContact.contact
                        val participant = participantWithContact.participant
                        val userId = contact.userId ?: continue

                        val newMemberItem = when {
                            contact.userId == _groupInformation.value?.createdBy ->
                                GroupMemberItem.GroupCreater(
                                    userId = contact.userId,
                                    name = contact.name,
                                    profileImage = contact.profilePic,
                                    statusText = contact.statusText
                                )
                            participant.role == "ADMIN" ->
                                GroupMemberItem.GroupAdmin(
                                    userId = contact.userId,
                                    name = contact.name,
                                    profileImage = contact.profilePic,
                                    statusText = contact.statusText
                                )
                            else ->
                                GroupMemberItem.GroupMember(
                                    name = contact.name,
                                    userId = contact.userId,
                                    statusText = contact.statusText,
                                    profileImage = contact.profilePic
                                )
                        }
                        updatedMembersMap[userId.toString()] = newMemberItem
                    }
                    _groupMembers.value = ArrayList(updatedMembersMap.values)
                    println("$LOG_TAG  Updated _groupMembers: ${_groupMembers.value}")
                    originalList.clear()
                    originalList.addAll(updatedMembersMap.values)
                }
        }
    }



    fun updateGroupInfo(conversationId : String, groupName: String, groupDescription: String) {
        println("Update GroupInformation for conversation with conversationId $conversationId , groupName $groupName , groupDescription $groupDescription")
        val groupInforfieldMap = HashMap<String,Any>()
        groupInforfieldMap["name"] = groupName
        groupInforfieldMap["description"] = groupDescription
        groupInforfieldMap["lastModifiedAt"] = System.currentTimeMillis()
        viewModelScope.launch {
            conversationRepository.updateConversation(conversationId = conversationId, groupInforfieldMap)
        }
        loadGroupInformation(conversationId)
    }

    fun removeMember(userId: String) {
        viewModelScope.launch {
            participantRepository.deleteParticipant(_currentConversationId.value, userId)
        }
    }

    fun makeAdmin(userId: String) {
        viewModelScope.launch {
            val roomParticipant = RoomParticipant(
                userId = userId,
                conversationId = _currentConversationId.value,
                role = "ADMIN"
            )
            participantRepository.updateRole(roomParticipant)
        }
    }

    fun removeAdmin(userId: String) {
        viewModelScope.launch {
            val roomParticipant = RoomParticipant(
                userId = userId,
                conversationId = _currentConversationId.value,
                role = "MEMBER"
            )
            participantRepository.updateRole(roomParticipant)
        }

    }
}