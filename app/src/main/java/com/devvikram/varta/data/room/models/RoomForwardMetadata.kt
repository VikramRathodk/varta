package com.devvikram.varta.data.room.models

class RoomForwardMetadata(
    val originalMessageId: String, // ID of the original message being forwarded
    val originalSenderId: String, // ID of the original sender
    val originalSenderName: String, // Display name of the original sender
    val originalConversationId: String, // ID of the conversation where the message originated
    val originalTimestamp: Long, // Time when the original message was sent
    val forwarderChain: List<String> = emptyList() // List of user IDs who forwarded the message
)