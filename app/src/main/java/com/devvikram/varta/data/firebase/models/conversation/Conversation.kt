package com.devvikram.varta.data.firebase.models.conversation

data class Conversation(
    var conversationId: String = "",
    val type: String ,//P (Personal Conversation), G (Group Conversation)
    val groupType: String? = null,//Project-Based, Company-Based, null(General-Group)
    val name: String? = null, // Optional, for group chats
    val createdBy: String,// User ID of the creator
    val createdAt: Long,
    val description: String? = null, // Group chat description
    val lastModifiedAt: Long = System.currentTimeMillis(),
    val participantIds : List<String> = emptyList(), // List of participant IDs
    val participants: List<Participant> = emptyList(), // List of participants
){
    constructor() : this("", "", "", "", "", 0, "", System.currentTimeMillis(),emptyList(),emptyList())
}
