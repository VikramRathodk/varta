package com.devvikram.varta.data.firebase.models.conversation

data class UserPreference(
    val isPinned: Boolean = false,
    val customNotificationTone: String? = null // Path to custom tone or system default
)
