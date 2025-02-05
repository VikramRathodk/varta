package com.devvikram.varta.ui.screens.sendFiles

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SendFileViewmodel : ViewModel() {


    private val _conversationName = MutableStateFlow("")
    val conversationName: StateFlow<String> = _conversationName

    private val _selectedFileUri = MutableStateFlow<MutableList<Uri>?>(null)
    val selectedFileUri: StateFlow<MutableList<Uri>?> = _selectedFileUri.asStateFlow()

    private val _currentUriIndex = MutableStateFlow(0)
    val currentUriIndex: StateFlow<Int> = _currentUriIndex.asStateFlow()

    private fun moveToNextUri(onExitScreen: () -> Unit) {
        _selectedFileUri.value?.let { files ->
            if (files.isNotEmpty()) {
                if (_currentUriIndex.value < files.size - 1) {
                    _currentUriIndex.value++
                } else {
                    onExitScreen()
                }
            }
        }
    }

    fun sendSelectedFileUri(selectedFileUri: MutableList<Uri>) {
        _selectedFileUri.value = selectedFileUri
        _currentUriIndex.value = 0
    }
    fun updateConversationName(conversationName: String) {
        _conversationName.value = conversationName
    }

    fun sendMessage(message: String, onExitScreen: () -> Unit){
        println("Message: $message")
        moveToNextUri(onExitScreen = onExitScreen)
    }
}

