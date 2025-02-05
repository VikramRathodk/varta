package com.devvikram.varta.data.firebase.models.message

import com.devvikram.varta.data.firebase.models.enums.MessageType


data class ChatMessage(
    val messageId: String, // Unique ID for the message
    val conversationId: String, // To associate the message with the conversation
    val senderId: String, // ID of the sender
    val senderName: String? = null, // Optional sender name for quick display
    val messageType: MessageType, // Enum to indicate message type (TEXT, IMAGE, VIDEO, etc.)
    val text: String? = null, // Message text (null for media/system messages)
    val timestamp: Long, // Time when the message was sent
    val mediaUrl: String? = null, // URL for media files (images, videos, audio, documents)
    val thumbnailUrl: String? = null, // Thumbnail URL for video/image previews
    val mediaSize: Long? = null, // File size in bytes for media (optional)
    val mediaType: String? = null, // MIME type for media files
    val mediaName: String? = null, // Original file name for media files
    val mediaDurationInSeconds: Int? = null, // Duration for audio/video files
    val isEdited: Boolean = false, // Flag to indicate if the message has been edited
    val reactions: Map<String, String> = emptyMap(), // Key: userId, Value: emoji (for reactions)
    val isReadBy: Map<String, Long> = emptyMap(), // Key: userId, Value: read status
    val isReceivedBy: Map<String, Long> = emptyMap(), // Key: userId, Value: received status
    val mentions: List<String> = emptyList(), // List of userIds who were mentioned in the message
    val replyToMessageId: String? = null, // Message ID of the original message being replied to
    val deletedForUsers: List<String> = emptyList(), // List of userIds for whom the message is deleted
    val isDeleted: Boolean = false, // Flag to indicate if the message was soft-deleted for everyone
    val isForwarded: Boolean = false, // Flag to indicate if the message was forwarded
    val forwardMetadata: ForwardMetadata? = null,// Metadata for forwarded messages
    val forwardCount: Int = 0, // Number of times the message was forwarded
){
    constructor() : this(
        messageId = "",
        conversationId = "",
        senderId = "",
        senderName = null,
        messageType = MessageType.TEXT,
        text = null,
        timestamp = 0L,
        mediaUrl = null,
        thumbnailUrl = null,
        mediaSize = null,
        mediaType = null,
        mediaName = null,
        mediaDurationInSeconds = null,
        isEdited = false,
        reactions = emptyMap(),
        isReadBy = emptyMap(),
        isReceivedBy = emptyMap(),
        mentions = emptyList(),
        replyToMessageId = null,
        deletedForUsers = emptyList(),
        isDeleted = false,
        isForwarded = false,
        forwardMetadata = null,
        forwardCount = 0
    )
}
