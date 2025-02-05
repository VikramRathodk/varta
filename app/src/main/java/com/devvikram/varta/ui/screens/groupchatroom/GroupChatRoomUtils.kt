package com.devvikram.varta.ui.screens.groupchatroom

import androidx.compose.foundation.lazy.LazyListState
import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GroupChatRoomUtils{
    companion object{
        fun createSenderMessageForGroup(message: RoomMessage): List<GroupChatMessageItem> {
            return listOf(
                when (message.messageType) {
                    MessageType.TEXT.toString() -> GroupChatMessageItem.GSenderTextChatItem(
                        messageId = message.messageId,
                        content = message.text.toString(),
                        replyMessageId = message.replyToMessageId.toString(),
                        isFavorite = false,
                        isForwared = false,
                        isReply = false,
                        replyMessage = "",
                        replyFileName = "",
                        timestamp = message.timestamp.toString(),
                        messageStatus = "",
                        participants = "1",
                        fileName = "",
                        isMediaMessage = false,
                        mediaType = message.mediaType.toString(),
                    )

                    else -> {
                        error("GroupChatRoomUtils.createSenderMessageForGroup")
                    }
                }
            )
        }

        fun createReceiverMessageForGroup(message: RoomMessage): List<GroupChatMessageItem> {
            return listOf(
                when (message.messageType) {
                    MessageType.TEXT.toString() -> GroupChatMessageItem.GReceiverTextChatItem(
                        messageId = message.messageId,
                        content = message.text.toString(),
                        replyMessageId = message.replyToMessageId.toString(),
                        isFavorite = false,
                        isForwared = false,
                        isReply = false,
                        replyMessage = "",
                        replyFileName = "",
                        timestamp = message.timestamp.toString(),
                        messageStatus = "",
                        participants = "1",
                        fileName = "",
                        isMediaMessage = false,
                        mediaType = message.mediaType.toString(),
                    )

                    else -> {
                        error("GroupChatRoomUtils.createReceiverMessageForGroup")
                    }
                }
            )
        }

        fun findMessageById(
            messageId: String,
            messageWithMediaList: List<GroupChatMessageItem>
        ): GroupChatMessageItem? {
            return messageWithMediaList.find { it.messageId == messageId }
        }

        fun handleReply(
            messageItem: GroupChatMessageItem,
            coroutineScope: CoroutineScope,
            messageWithMediaList: List<GroupChatMessageItem>,
            listState: LazyListState,
            onBackgroundSelectionChange: (GroupChatMessageItem, Boolean) -> Unit = { _, _ -> }

        ) {
            val replyMessageId = messageItem.replyMessageId
            val replyMessage = findMessageById(
                replyMessageId,
                messageWithMediaList = messageWithMediaList
            )

            if (replyMessage != null) {
                println("| Reply Section Clicked: Found reply message: $replyMessage")
                coroutineScope.launch {
                    val replyMessageIndex = messageWithMediaList.indexOf(replyMessage)
                    if (replyMessageIndex >= 0) {
                        listState.animateScrollToItem(replyMessageIndex)
                        val selectedItem = messageWithMediaList[replyMessageIndex]
                        onBackgroundSelectionChange(selectedItem, true)
                    }
                }
            } else {
                println("| Reply Section Clicked: Reply message not found!")
            }
        }
    }
}