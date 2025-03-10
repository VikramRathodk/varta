package com.devvikram.varta.data.firebase.models.conversation

import com.devvikram.varta.data.firebase.models.enums.MessageType

data class LastMessage(
    val messageId: String,
    val senderId: String,
    val senderName: String, // Display name of the sender
    val messageType: MessageType,
    val text: String? = null, // Message text preview (can be null for media-only messages)
    val timestamp: Long,
    val mediaType: String? = null, // "image", "video", "audio", "file", or null for text messages
    val mediaUrl: String? = null, // URL for media content (can be null for text messages)
    val isReadBy: Map<String, Long>, // Map of user IDs to timestamp when the message was read
    val isReceivedBy: Map<String, Long>, // Map of user IDs to timestamp when the message was received
)
