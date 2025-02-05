package com.devvikram.varta.ui.screens.contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val contactRepository: ContactRepository
) : ViewModel() {

    val contacts: StateFlow<List<ProContacts>> =
        contactRepository.getAllUsersContacts().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
