package com.devvikram.varta.ui.screens.personalchatroom

import androidx.compose.foundation.lazy.LazyListState
import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem
import com.devvikram.varta.utility.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PersonalChatRoomUtil{
    companion object{

fun createSenderMessage(message: RoomMessage): List<PersonalChatMessageItem> {
    return listOf(
        when (message.messageType) {
            MessageType.TEXT.toString() -> PersonalChatMessageItem.PSenderTextChatItem(
                messageId = message.messageId,
                content = message.text.toString(),
                replyMessageId = message.replyToMessageId.toString(),
                isFavorite = false,
                filename = "",
                mediaType = message.mediaType.toString(),
                isMediaMessage = false,
                timestamp = AppUtils.getTimeInHoursAndMinutes(message.timestamp)

            )
            else -> error("Unsupported message type: ${message.messageType}")
        }
    )
}
        fun createReceiverMessage(
            message: RoomMessage
        ): List<PersonalChatMessageItem> {
            return listOf(
                when (message.messageType) {
                    MessageType.TEXT.toString() -> PersonalChatMessageItem.PReceiverTextChatItem(
                        messageId = message.messageId,
                        content = message.text.toString(),
                        replyMessageId = message.replyToMessageId.toString(),
                        filename = "",
                        mediaType = message.mediaType.toString(),
                        isMediaMessage = false,
                        timestamp = message.timestamp.toString()

                    )
                    else -> error("Unsupported message type: ${message.messageType}")
                }
            )
        }

        fun handleReply(
            messageItem: PersonalChatMessageItem,
            coroutineScope: CoroutineScope,
            messageWithMediaList: List<PersonalChatMessageItem>,
            listState: LazyListState,
            onBackgroundSelectionChange: (PersonalChatMessageItem, Boolean) -> Unit = { _, _ -> }

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

        fun findMessageById(
            messageId: String,
            messageWithMediaList: List<PersonalChatMessageItem>
        ): PersonalChatMessageItem? {
            return messageWithMediaList.find { it.messageId == messageId }
        }


    }
}