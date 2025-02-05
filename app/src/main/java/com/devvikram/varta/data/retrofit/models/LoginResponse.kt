package com.devvikram.varta.data.retrofit.models

import com.devvikram.varta.data.room.models.ProContacts


data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: UserInformation? = null,
)
data class UserInformation(
    val sessionId: Int,
    val sessionStatus : Boolean = false,
    val userData: ProContacts
)
