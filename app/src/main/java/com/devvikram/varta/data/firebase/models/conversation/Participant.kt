package com.devvikram.varta.data.firebase.models.conversation

data class Participant(
    val userId: String,
    val role: String = "MEMBER" // Optional, applicable for group chats (e.g., "admin", "member")
){
    constructor() : this("", "")

    override fun toString(): String {
        return "Participant(userId='$userId', role='$role')"
    }
}
