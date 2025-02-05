package com.devvikram.varta.ui.screens.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.firebase.models.conversation.Participant
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.models.RoomConversation
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.ui.itemmodels.ContactMultipleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val conversationRepository: ConversationRepository,
    private val loginPreference: LoginPreference

)
: ViewModel() {
    fun createGroup(groupName : String,groupDescription : String,selectedContacts: List<ContactMultipleItem.ContactItem>) {

        viewModelScope.launch {

            val participantsIds = selectedContacts.map { it.contact.userId.toString() } as ArrayList<String>
            participantsIds.add(loginPreference.getUserId())

            val conversation = RoomConversation(
                conversationId = "",
                userId = 0,
                type = "G",
                name = groupName,
                createdAt = System.currentTimeMillis(),
                createdBy = loginPreference.getUserId() ,
                groupType = null,
                lastModifiedAt = System.currentTimeMillis(),
                participantIds = participantsIds,
                description = groupDescription,
            )
            val roomParticipants = participantsIds.map {
                RoomParticipant(
                    localParticipantId = 0,
                    userId = it,
                    conversationId = conversation.conversationId,
                    role = "MEMBER"
                )
            }
            val participants = participantsIds.map {
                Participant(
                    userId = it,
                    role = "MEMBER"
                )
            }


            conversationRepository.createConversation(
                conversation,
                roomParticipants,
                participants,
            )
        }
    }

    val contacts: StateFlow<List<ProContacts>> =
        contactRepository.getAllUsersContacts().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}