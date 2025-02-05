package com.devvikram.varta.ui.screens.groupchatroom.groupinfo

import androidx.lifecycle.ViewModel
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.data.room.repository.ParticipantRepository
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupMemberItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GroupInfoViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val contactRepository: ContactRepository,
    private val participantRepository: ParticipantRepository

): ViewModel() {
    private val LOG_TAG = "GROUP_INFO"

    private val _isEditDialogOpen = MutableStateFlow(false)
    val isEditDialogOpen: StateFlow<Boolean> = _isEditDialogOpen.asStateFlow()

    private val _currentConversationId = MutableStateFlow("")
    val currentConversationId: StateFlow<String> = _currentConversationId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _groupMembers = MutableStateFlow<ArrayList<GroupMemberItem>>(ArrayList())
    val groupMembers: StateFlow<ArrayList<GroupMemberItem>> = _groupMembers.asStateFlow()


    private val originalList = ArrayList<GroupMemberItem>()


    fun setCurrentConversationId(conversationId: String) {
        _currentConversationId.value = conversationId
    }



    fun updateSearchQuery(searchQuery: String) {
        _searchQuery.value = searchQuery
        filterGroupMembers(searchQuery)
    }

    private fun filterGroupMembers(query: String) {
        val allMembers = if (query.isEmpty()) {
            originalList
        } else {
            val filteredList = ArrayList<GroupMemberItem>()
            filteredList.addAll(originalList.filterIsInstance<GroupMemberItem.GroupAddNewMember>())
            filteredList.addAll(originalList.filter {
                when (it) {
                    is GroupMemberItem.GroupMember -> it.name.contains(query, ignoreCase = true)
                    is GroupMemberItem.GroupAdmin -> it.name.contains(query, ignoreCase = true)
                    else -> false
                }
            })
            filteredList
        }
        _groupMembers.value = ArrayList(allMembers)
    }
    fun updateGroupMembers(groupMembers: ArrayList<GroupMemberItem>){
        _groupMembers.value = groupMembers
        originalList.clear()
        originalList.addAll(groupMembers)
    }


    fun updateEditDialogVisibility(visibility: Boolean) {
        _isEditDialogOpen.value = visibility
    }

}
