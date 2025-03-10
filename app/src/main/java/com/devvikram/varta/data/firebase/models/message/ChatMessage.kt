package com.devvikram.varta.data.firebase.models.message

import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.google.firebase.firestore.PropertyName


data class ChatMessage(
    val messageId: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderName: String? = null,
    val messageType: MessageType = MessageType.TEXT,
    val text: String? = null,
    val timestamp: Long = 0L,
    val mediaUrl: String? = null,
    val thumbnailUrl: String? = null,
    val mediaSize: Long? = null,
    val mediaType: String? = null,
    val mediaName: String? = null,
    val mediaDurationInSeconds: Int? = null,

    @PropertyName("edited") @JvmField
    val isEdited: Boolean = false,

    val reactions: Map<String, String> = emptyMap(),

    @PropertyName("readBy") @JvmField
    val isReadBy: Map<String, Long> = emptyMap(),

    @PropertyName("receivedBy") @JvmField
    val isReceivedBy: Map<String, Long> = emptyMap(),

    val mentions: List<String> = emptyList(),
    val replyToMessageId: String? = null,
    val deletedForUsers: List<String> = emptyList(),

    @PropertyName("deleted") @JvmField
    val isDeleted: Boolean = false,

    @PropertyName("forwarded") @JvmField
    val isForwarded: Boolean = false,

    val forwardMetadata: ForwardMetadata? = null,
    val forwardCount: Int = 0,

    val lastModifiedAt: Long = System.currentTimeMillis(),

    val mediaStatus: String = "IDEAL",
    val uploadProgress: Int = 0,
    val downloadProgress: Int = 0,

    @PropertyName("downloaded") @JvmField
    val isDownloaded: Boolean = false,

    @PropertyName("uploaded") @JvmField
    val isUploaded: Boolean = false
)