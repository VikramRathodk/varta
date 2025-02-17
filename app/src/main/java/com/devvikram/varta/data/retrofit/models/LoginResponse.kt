package com.devvikram.varta.data.retrofit.models

import com.devvikram.varta.data.room.models.ProContacts


data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: MutableMap<String, Any>? = null,
)
