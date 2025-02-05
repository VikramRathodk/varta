package com.devvikram.varta.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val loginPreference: LoginPreference
):ViewModel(){

    val currentLoggedUser : StateFlow<ProContacts?> = contactRepository.getContactByUserIdFlow(loginPreference.getUserId()).stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    private val _isLogoutVisible = MutableStateFlow(false)
    val isLogoutVisible = _isLogoutVisible.asStateFlow()

    private val _isHomeToolbarVisible = MutableStateFlow(true)
    val isHomeToolbarVisible = _isHomeToolbarVisible.asStateFlow()


    init {
        setIsLogoutVisible(false)
    }

    fun setIsHomeToolbarVisible(isHomeToolbarVisible: Boolean) {
        _isHomeToolbarVisible.value = isHomeToolbarVisible
    }

    fun setIsLogoutVisible(isLogoutVisible: Boolean) {
        _isLogoutVisible.value = isLogoutVisible
    }

}