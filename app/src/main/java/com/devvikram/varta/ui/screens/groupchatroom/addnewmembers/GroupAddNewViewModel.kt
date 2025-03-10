package com.devvikram.varta.ui.screens.groupchatroom.addnewmembers

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.data.firebase.models.conversation.Participant
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ParticipantRepository
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupContactItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupAddNewViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val participantRepository: ParticipantRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _conversationId = MutableStateFlow("")
    val conversationId: StateFlow<String> = _conversationId

    val contacts: StateFlow<List<ProContacts>> = contactRepository.getAllUsersContacts().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _contactItems = MutableStateFlow<List<GroupContactItem>>(emptyList())
    val contactItems: StateFlow<List<GroupContactItem>> = _contactItems

    private val _selectedContactItems = MutableStateFlow<List<GroupContactItem.SelectedGroupContactItem>>(emptyList())
    val selectedContactItems: StateFlow<List<GroupContactItem.SelectedGroupContactItem>> = _selectedContactItems

    private val originalContactItems = mutableListOf<GroupContactItem>()


    private fun observeContacts() {
        viewModelScope.launch {
            combine(
                participantRepository.getParticipantsByConversationIdWithFlow(_conversationId.value),
                contacts
            ) { alreadyGroupParticipants, contactsList ->
                contactsList.map { contact ->
                    val alreadyAdded = alreadyGroupParticipants.any { it.userId == contact.userId }

                    Log.d(TAG, "handleContacts: Contact ${contact.name} - Already Added: $alreadyAdded")

                    if (alreadyAdded) {
                        GroupContactItem.AlreadyAddedGroupContactItem(
                            localProfilePath = contact.localProfilePicPath,
                            statusText = contact.statusText,
                            proUserId = contact.userId,
                            proNameOfUser = contact.name
                        )
                    } else {
                        GroupContactItem.UnselectedGroupContactItem(
                            localProfilePath = contact.localProfilePicPath,
                            isSelected = false,
                            statusText = contact.statusText,
                            proUserId = contact.userId,
                            proNameOfUser = contact.name
                        )
                    }
                }
            }.collect { updatedItems ->
                _contactItems.value = updatedItems
                originalContactItems.clear()
                originalContactItems.addAll(updatedItems)
            }
        }
    }



    fun toggleSelection(item: GroupContactItem) {
        val updatedItems = _contactItems.value.map { currentItem ->
            when (currentItem) {
                is GroupContactItem.UnselectedGroupContactItem -> {
                    if (currentItem.proUserId == item.proUserId) {
                        val selectedItem = GroupContactItem.SelectedGroupContactItem(
                            proUserId = currentItem.proUserId,
                            proNameOfUser = currentItem.proNameOfUser,
                            statusText = currentItem.statusText,
                            isSelected = !currentItem.isSelected,
                            localProfilePath = currentItem.localProfilePath
                        )
                        updateSelectedItems(selectedItem)
                        selectedItem
                    } else {
                        currentItem
                    }
                }
                is GroupContactItem.SelectedGroupContactItem -> {
                    if (currentItem.proUserId == item.proUserId) {
                        val unselectedItem = GroupContactItem.UnselectedGroupContactItem(
                            proUserId = currentItem.proUserId,
                            proNameOfUser = currentItem.proNameOfUser,
                            statusText = currentItem.statusText,
                            isSelected = !currentItem.isSelected,
                            localProfilePath = currentItem.localProfilePath
                        )
                        removeSelectedItem(currentItem)
                        unselectedItem
                    } else {
                        currentItem
                    }
                }
                is GroupContactItem.AlreadyAddedGroupContactItem -> currentItem
            }
        }
        _contactItems.value = updatedItems
    }

    private fun updateSelectedItems(item: GroupContactItem.SelectedGroupContactItem) {
        _selectedContactItems.value += item
        println("Selected members are ${_selectedContactItems.value.size}")
    }

    private fun removeSelectedItem(item: GroupContactItem.SelectedGroupContactItem) {
        _selectedContactItems.value = _selectedContactItems.value.filter { it.proUserId != item.proUserId }
        println("Selected members are ${_selectedContactItems.value.size}")
    }

    fun addSelectedMembersToGroup(conversationId : String) {
        val selectedItems = _contactItems.value.filterIsInstance<GroupContactItem.SelectedGroupContactItem>()
        _selectedContactItems.value = selectedItems
        println("Selected members are ${_selectedContactItems.value.size}")
        val roomParticipantsList = mutableListOf<RoomParticipant>()
        val participantsList = mutableListOf<Participant>()

        for(item in selectedItems){
            val participant = Participant(
                userId = item.proUserId.toString(),
                role = "MEMBER"
            )
            val roomParticipant = RoomParticipant(
                localParticipantId = 0,
                userId = item.proUserId.toString(),
                conversationId = conversationId,
                role = "MEMBER"
            )
            participantsList.add(participant)
            roomParticipantsList.add(roomParticipant)
        }
        participantRepository.insertNewParticipants(conversationId,roomParticipantsList,participantsList)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterContacts(query)
    }

    private fun filterContacts(query: String) {
        print("Query: addnew  $query")
        val filteredContacts = if (query.isEmpty()) {
            originalContactItems
        } else {
            originalContactItems.filter { contact ->
                when (contact) {
                    is GroupContactItem.UnselectedGroupContactItem -> contact.proNameOfUser.contains(query, ignoreCase = true)
                    is GroupContactItem.SelectedGroupContactItem -> contact.proNameOfUser.contains(query, ignoreCase = true)
                    is GroupContactItem.AlreadyAddedGroupContactItem -> contact.proNameOfUser.contains(query, ignoreCase = true)
                }
            }
        }
        _contactItems.value = filteredContacts
    }

    fun updateConversationId(conversationId: String) {
        if (_conversationId.value != conversationId) {
            Log.d(TAG, "updateConversationId:  $conversationId")
            _conversationId.value = conversationId
            observeContacts()
        }
    }


}
