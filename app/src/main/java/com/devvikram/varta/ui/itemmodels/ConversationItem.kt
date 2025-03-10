package com.devvikram.varta.ui.itemmodels

sealed class ConversationItem {
    abstract val conversationId: String
    abstract val lastMessage: String
    abstract val lastMessageTime: String
    abstract val lastMessageSender: Long
    abstract val lastMessageStatus: String
    abstract val unreadMessageCount: Int
    abstract val isMediaMessage: Boolean
    abstract val mediaType: String
    abstract val selected: Boolean

    data class PersonalConversation(
        override val conversationId: String = "",
        val proContactId: Long = 0L,
        val proContactName: String = "",
        override val lastMessage: String = "",
        override val lastMessageTime: String = "",
        override val lastMessageSender: Long = 0L,
        override val lastMessageStatus: String = "",
        override val unreadMessageCount: Int = 0,
        val gender: Int = 1,
        override val isMediaMessage: Boolean = false,
        override val mediaType: String = "",
        val localProfilePicPath: String = "https://",
        override var selected: Boolean = false,
        val isForwarded: Boolean = false,
        val proContactstatusText: String = ""
    ) : ConversationItem()

    data class GroupConversation(
        override val conversationId: String = "",
        val groupName: String = "",
        val groupIconUrl: String = "",
        val groupIconLocalPath: String = "",
        override val lastMessage: String = "",
        override val lastMessageTime: String = "",
        override val lastMessageSender: Long = 0L,
        override val lastMessageStatus: String = "",
        override val unreadMessageCount: Int = 0,
        val gender: Int = 1,
        val isExited: Boolean = false,
        override val isMediaMessage: Boolean = false,
        override val mediaType: String = "",
        override var selected: Boolean = false,
        val isForwarded: Boolean = false,
        val groupDescription: String = "",
        val participants: List<String> = arrayListOf(),
        val createdBy : String = ""
    ) : ConversationItem()
}