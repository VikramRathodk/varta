package com.devvikram.varta.data.firebase.models.conversation

data class Role(
    val roleName: String,
    val canPost: Boolean,
    val canDeleteMessages: Boolean,
    val canManageParticipants: Boolean
)
