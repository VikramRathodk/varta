package com.devvikram.varta.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class RoomMessage(
    @PrimaryKey val messageId: String,
    val conversationId: String, // Foreign key reference to conversations
    val senderId: String,
    val senderName: String? = null, // Optional display name of the sender
    val messageType: String, // TEXT, IMAGE, VIDEO, etc.
    val text: String? = null, // Message text (optional for media-only messages)
    val timestamp: Long, // Message timestamp in epoch milliseconds
    val mediaUrl: String? = null, // URL for media files
    val thumbnailUrl: String? = null, // Thumbnail URL for media previews
    val mediaSize: Long? = null, // File size in bytes for media
    val mediaType: String? = null, // MIME type for media files
    val mediaName: String? = null, // Original file name for media files
    val mediaDurationInSeconds: Int? = null, // Duration for audio/video files
    val isEdited: Boolean = false,
    val reactions: Map<String, String> = emptyMap(), // Key: userId, Value: emoji
    val isReadBy: Map<String, Long> = emptyMap(), // Key: userId, Value: read timestamp
    val isReceivedBy: Map<String, Long> = emptyMap(), // Key: userId, Value: received timestamp
    val mentions: List<String> = emptyList(), // List of userIds who were mentioned
    val replyToMessageId: String? = null, // Message ID being replied to
    val deletedForUsers: List<String> = emptyList(), // List of userIds for whom the message is deleted
    val isDeleted: Boolean = false, // Flag to indicate if the message was soft-deleted for everyone
    val isForwarded: Boolean = false, // Flag to indicate if the message was forwarded
    val forwardMetadata: RoomForwardMetadata? = null, // Metadata for forwarded messages
    val forwardCount: Int = 0, // Number of times the message was forwarded
    val localFilePath: String? = null, // Local path for downloaded media files
    val isDownloaded: Boolean = false, // Flag indicating if the media file has been downloaded
    val isUploaded: Boolean = false, // Flag indicating if the media file has been uploaded
    val uploadProgress: Int = 0,// Upload progress percentage (0-100)
    val downloadProgress: Int = 0,// Download progress percentage (0-100)
    val isFailed: Boolean = false, // Flag indicating if the message failed to send
    val lastModifiedAt: Long = System.currentTimeMillis()
)
