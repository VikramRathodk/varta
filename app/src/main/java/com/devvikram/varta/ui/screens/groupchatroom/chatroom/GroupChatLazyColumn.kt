package com.devvikram.varta.ui.screens.groupchatroom.chatroom

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import com.devvikram.varta.ui.screens.groupchatroom.GroupChatRoomUtils.Companion.findMessageById
import com.devvikram.varta.ui.screens.groupchatroom.GroupChatRoomUtils.Companion.handleReply
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupReceiverAutoCadChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupReceiverExcelChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupReceiverImageChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupReceiverPdfChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupReceiverTextChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupReceiverWordChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupSenderAutoCadChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupSenderExcelChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupSenderImageChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupSenderPdfChatItemCard
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupSenderTextChatItemView
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews.GroupSenderWordChatItemCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupChatLazyColumn(
    modifier: Modifier,
    messageWithMediaList: List<GroupChatMessageItem>,
    onIsSelectionMode: (Boolean) -> Unit,
    onReplyMode: (Boolean) -> Unit,
    onSelectedList: (Set<GroupChatMessageItem>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedMessageList by remember { mutableStateOf<Set<GroupChatMessageItem>>(emptySet()) }
    var isReplyMode by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()


    LaunchedEffect(isSelectionMode) {
        onIsSelectionMode(isSelectionMode)
    }
    LaunchedEffect(isReplyMode) {
        onReplyMode(isReplyMode)
    }
    LaunchedEffect(selectedMessageList) {
        onSelectedList(selectedMessageList)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .nestedScroll(rememberNestedScrollInteropConnection())
    ) {
        items(
            messageWithMediaList.size,
            key = { index -> messageWithMediaList[index].hashCode() }) { messageWithMedia ->
            val messageItem = messageWithMediaList[messageWithMedia]
            var offsetX by remember { mutableFloatStateOf(0f) }
            var totalDragDistance by remember { mutableFloatStateOf(0f) }
            var isReplyTriggered by remember { mutableStateOf(false) }
            val hapticFeedback = LocalHapticFeedback.current
            val dragThreshold = 100f
            val screenWidth =
                LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.dp.toPx() }
            val maxDragDistance = screenWidth * 0.5f
            Log.d(TAG, "PersonalChatRoomScreen: MessageItem: $messageItem")
            Box(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, dragAmount ->
                                if (dragAmount > 0) {
                                    totalDragDistance += dragAmount
                                    offsetX =
                                        (offsetX + dragAmount).coerceIn(0f, maxDragDistance)

                                    if (totalDragDistance >= dragThreshold && !isReplyTriggered) {
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        isReplyTriggered = true
                                    }
                                }
                            },
                            onDragEnd = {
                                if (isReplyTriggered) {
                                    isReplyMode = true
                                    selectedMessageList = setOf(messageItem)
                                }
                                totalDragDistance = 0f
                                isReplyTriggered = false

                                offsetX = 0f
                            },
                            onDragCancel = {
                                totalDragDistance = 0f
                                isReplyTriggered = false
                                offsetX = 0f
                            }
                        )
                    }
                    .graphicsLayer {
                        translationX = offsetX
                    }
                    .combinedClickable(
                        onClick = {
                            if (isSelectionMode) {
                                selectedMessageList =
                                    if (selectedMessageList.contains(messageItem)) {
                                        selectedMessageList - messageItem
                                    } else {
                                        selectedMessageList + messageItem
                                    }
                                isSelectionMode = selectedMessageList.isNotEmpty()
                            }
                        },
                        onLongClick = {
                            isSelectionMode = true
                            selectedMessageList =
                                if (selectedMessageList.contains(messageItem)) {
                                    selectedMessageList - messageItem
                                } else {
                                    selectedMessageList + messageItem
                                }
                            isSelectionMode = selectedMessageList.isNotEmpty()
                        }
                    )
            ) {

                if (totalDragDistance > 0f) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp)
                    ) {
                        if (isReplyTriggered) {
                            // Display the reply icon when swipe threshold is passed
                            Icon(
                                painter = painterResource(id = R.drawable.reply_icon),
                                contentDescription = "Reply",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                val replyMessageItem = findMessageById(
                    messageId = messageItem.replyMessageId,
                    messageWithMediaList = messageWithMediaList
                )
                when (messageItem) {
                    is GroupChatMessageItem.GSenderTextChatItem -> {
                        GroupSenderTextChatItemView(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem,
                            onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GSenderImageChatItem -> {
                        GroupSenderImageChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GSenderPdfChatItem -> {
                        GroupSenderPdfChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GSenderExcelChatItem -> {
                        GroupSenderExcelChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GSenderWordChatItem -> {
                        GroupSenderWordChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GSenderAutoCadChatItem -> {
                        GroupSenderAutoCadChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GReceiverTextChatItem -> {
                        GroupReceiverTextChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GReceiverImageChatItem -> {
                        GroupReceiverImageChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GReceiverPdfChatItem -> {
                        GroupReceiverPdfChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GReceiverExcelChatItem -> {
                        GroupReceiverExcelChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GReceiverWordChatItem -> {
                        GroupReceiverWordChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }

                    is GroupChatMessageItem.GReceiverAutoCadChatItem -> {
                        GroupReceiverAutoCadChatItemCard(
                            messageItem,
                            isSelected = selectedMessageList.contains(messageItem),
                            replyMessageItem = replyMessageItem, onReplySectionClick = {
                                handleReply(
                                    messageItem = messageItem,
                                    coroutineScope = coroutineScope,
                                    messageWithMediaList = messageWithMediaList,
                                    listState = listState,
                                    onBackgroundSelectionChange = { item, state ->
                                        selectedMessageList =
                                            if (selectedMessageList.contains(item)) {
                                                selectedMessageList - item
                                            } else {
                                                selectedMessageList + item
                                            }
                                        // remove after 2 sec
                                        coroutineScope.launch {
                                            delay(2000)
                                            selectedMessageList = selectedMessageList - item
                                        }
                                    }
                                )
                            }
                        )
                    }
                }
            }

        }
    }
}