package com.devvikram.varta.ui.itemmodels

sealed class PersonalChatMessageItem {
    abstract val filename: String
    abstract val mediaType: String
    abstract val isMediaMessage: Boolean
    abstract val replyMessageId: String
    abstract val messageId: String
    abstract val content: String
    open val senderName: String = ""


    data class PSenderTextChatItem(
        override val messageId: String,
        override val replyMessageId: String,
        override val content: String,
        val isForwared: Boolean = false,
        val isReply: Boolean = false,
        val replyMessage: String = "",
        val replyFileName: String = "",
        val timestamp: String = "",
        val messageStatus: String = "",
        val isFavorite: Boolean,
        var isSelected: Boolean = false,
        override val senderName: String = "",
        override val filename: String,
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()
    data class PSenderImageChatItem(

        override val messageId: String,
        override val replyMessageId: String,
        override val content: String,
        val imageUrl: String,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean,
        val isFavorite: Boolean,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val filename: String,
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PSenderPdfChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        override val content: String,
        val pdfUrl: String,
        val isFavorite: Boolean,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean,
        override val filename: String = "",
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PSenderExcelDocChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        val excelDocUrl: String,
        val isFavorite: Boolean,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean,
        override val filename: String = "",
        override val content: String,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PSenderWordDocChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        val wordDocUrl: String,
        val isFavorite: Boolean,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean,
        override val filename: String = "",
        override val content: String,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PSenderAutoCadChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        val autoCadUrl: String,
        val isFavorite: Boolean,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean,
        override val content: String,
        override val filename: String = "",
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PReceiverTextChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        override val content: String,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean = false,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val filename: String,
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PReceiverImageChatItem(
        override val messageId: String,
        override val replyMessageId: String,
        val imageUrl: String,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean = false,
        override val content: String,
        val isFavorite: Boolean = false,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val filename: String,
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PReceiverPdfChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        val pdfUrl: String,
        val isFavorite: Boolean = false,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean = false,
        override val filename: String = "",
        override val content: String,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean

    ) : PersonalChatMessageItem()

    data class PReceiverExcelDocChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        val excelDocUrl: String,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean = false,
        override val filename: String = "",
        override val content: String,
        val isFavorite: Boolean = false,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PReceiverWordDocChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        val wordDocUrl: String,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean = false,
        override val filename: String = "",
        override val content: String,
        val isFavorite: Boolean = false,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

    data class PReceiverAutoCadChatItem(
        override val messageId: String,
        override val replyMessageId: String,

        val autoCadUrl: String,
        val timestamp: String = "",
        val messageStatus: String = "",
        val isForwared: Boolean = false,
        override val content: String,
        override val filename: String = "",
        val isFavorite: Boolean = false,
        val isSelected: Boolean = false,
        override val senderName: String = "",
        override val mediaType: String,
        override val isMediaMessage: Boolean
    ) : PersonalChatMessageItem()

//    data class PSenderAudioChatItem(val audio: String) : PersonalChatMessageItem()
//    data class PReceiverAudioChatItem(val audio: String) : PersonalChatMessageItem()
//    data class PSenderVideoChatItem(val video: String) : PersonalChatMessageItem()
//    data class PReceiverVideoChatItem(val video: String) : PersonalChatMessageItem()

//    data class PSenderLocationChatItem(val location: String) : PersonalChatMessageItem()
//    data class PReceiverLocationChatItem(val location: String) : PersonalChatMessageItem()
//    data class PSenderContactChatItem(val contact: String) : PersonalChatMessageItem()
//    data class PReceiverContactChatItem(val contact: String) : PersonalChatMessageItem()

}