package com.devvikram.varta.ui.screens.conversations

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.room.models.ConversationWithContact
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.ui.itemmodels.FilterOptionInConversation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ConversationViewmodel @Inject constructor (
    private val conversationRepository: ConversationRepository,
    private val contactRepository: ContactRepository,
    private val loginPreference: LoginPreference
): ViewModel(){

    val conversations: StateFlow<List<ConversationWithContact>> =
        conversationRepository.getAllConversationWithContact().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    val listOfFilterOptions = mutableStateListOf(
            FilterOptionInConversation("All", selected = true),
            FilterOptionInConversation("Read"),
            FilterOptionInConversation("Unread"),
            FilterOptionInConversation("Personal"),
            FilterOptionInConversation("Group"),
            FilterOptionInConversation("Media"),
            FilterOptionInConversation("Forwarded")
        )

}
