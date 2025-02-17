package com.devvikram.varta.data.firebase.models


import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class FContact(
    val userId: String = "",

    val name: String = "",

    val email: String = "",

    val gender: String = "",

    val designation: String = "",

    val profilePic: String = "",

    val userStatus: Boolean = false,

    val localProfilePicPath: String = ""
)

