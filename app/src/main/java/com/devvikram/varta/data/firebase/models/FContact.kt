package com.devvikram.varta.data.firebase.models

import kotlinx.serialization.Serializable

@Serializable
data class FContact(
    val userId: String = "",

    val name: String = "",

    val email: String = "",

    val gender: String = "",

    val profilePic: String = "",

    val userStatus: Boolean = false,

    val statusText: String = "Hey There ! I am using Varta"

)

