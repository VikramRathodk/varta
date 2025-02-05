package com.devvikram.varta.ui.screens.groupchatroom.chatroom.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
sealed class GroupChatMessageItem : Parcelable {
    abstract val mediaType: String
    abstract val isMediaMessage: Boolean
    abstract val participants: String
    abstract val replyMessageId: String
    abstract val messageId: String
    abstract val timestamp: String
    abstract val content: String
    open val senderName :String = ""
    abstract val fileName: String

    // Sender Chat Items
    data class GSenderTextChatItem(
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val messageId: String,
        override val replyMessageId: String,
        override val fileName: String, override val isMediaMessage: Boolean,
        override val mediaType: String,

        ) : GroupChatMessageItem()

    data class GSenderImageChatItem(
        val imageUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val fileName: String = "",
        override val messageId: String,
        override val replyMessageId: String, override val isMediaMessage: Boolean,
        override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GSenderPdfChatItem(
        val pdfUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val messageId: String, override val replyMessageId: String,
        override val fileName: String, override val isMediaMessage: Boolean,
        override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GSenderExcelChatItem(
        val excelUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val messageId: String, override val replyMessageId: String,
        override val fileName: String, override val isMediaMessage: Boolean,
        override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GSenderWordChatItem(
        val wordUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val messageId: String, override val replyMessageId: String,
        override val fileName: String, override val isMediaMessage: Boolean,
        override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GSenderAutoCadChatItem(
        val autoCadUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val messageId: String, override val replyMessageId: String,
        override val fileName: String, override val isMediaMessage: Boolean,
        override val mediaType: String,
    ) : GroupChatMessageItem()

    // Receiver Chat Items
    data class GReceiverTextChatItem(
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean = false,
        override val participants: String = "",
        override val senderName : String = "", override val messageId: String,
        override val replyMessageId: String, override val fileName: String,
        override val isMediaMessage: Boolean, override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GReceiverImageChatItem(
        val imageUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val senderName : String = "", override val messageId: String,
        override val replyMessageId: String, override val fileName: String,
        override val isMediaMessage: Boolean, override val mediaType: String,
        ) : GroupChatMessageItem()

    data class GReceiverPdfChatItem(
        val pdfUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val senderName : String = "", override val messageId: String,
        override val replyMessageId: String, override val fileName: String,
        override val isMediaMessage: Boolean, override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GReceiverExcelChatItem(
        val excelUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val senderName : String = "", override val messageId: String,
        override val replyMessageId: String, override val fileName: String,
        override val isMediaMessage: Boolean, override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GReceiverWordChatItem(
        val wordUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val senderName : String = "", override val messageId: String,
        override val replyMessageId: String, override val fileName: String,
        override val isMediaMessage: Boolean, override val mediaType: String,
    ) : GroupChatMessageItem()

    data class GReceiverAutoCadChatItem(
        val autoCadUrl: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        override val timestamp: String,
        val messageStatus: String = "",
        val isFavorite: Boolean,
        override val participants: String,
        override val senderName : String = "", override val messageId: String,
        override val replyMessageId: String, override val fileName: String,
        override val isMediaMessage: Boolean, override val mediaType: String,
    ) : GroupChatMessageItem()


}
