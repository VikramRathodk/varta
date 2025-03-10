package com.devvikram.varta.data.api.body

data class LoginBody(
    val deviceType: String = "MOBILE",
    val deviceUniqueId: String,
    val email: String,
    val force: Boolean = false,
    val password: String =""
)