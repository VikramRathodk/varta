package com.devvikram.varta.data.retrofit.models

data class LoginInformation (
    val email: String,
    val password: String ,
    val deviceType: String = "",
    val deviceUniqueId: String = "",
    val force: Boolean = false
)