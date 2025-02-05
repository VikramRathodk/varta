package com.devvikram.varta.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class RoomUserPreference(
    @PrimaryKey
    val localUserPreferenceId: Long,
    val conversationId: String, // Foreign key reference to conversation
    val isPinned: Boolean = false,
    val customNotificationTone: String? = null // Path to custom notification tone
)
