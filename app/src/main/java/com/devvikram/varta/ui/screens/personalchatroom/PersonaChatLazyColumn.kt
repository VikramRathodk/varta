package com.devvikram.varta.ui.screens.personalchatroom

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.receiver.ReceiverAutoCadChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.receiver.ReceiverExcelDocChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.receiver.ReceiverImageChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.receiver.ReceiverPdfChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.receiver.ReceiverTextChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.receiver.ReceiverWordDocChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.sender.SenderAutoCadChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.sender.SenderExcelDocChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.sender.SenderImageChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.sender.SenderPdfChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.sender.SenderTextChatItemCard
import com.devvikram.varta.ui.screens.personalchatroom.itemviews.sender.SenderWordDocChatItemCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonalChatRoomScreen(
    modifier: Modifier,
    messageWithMediaList: List<PersonalChatMessageItem>,
    onIsSelectionMode: (Boolean) -> Unit,
    onReplyMode: (Boolean) -> Unit,
    onSelectedList: (Set<PersonalChatMessageItem>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedMessageList by remember { mutableStateOf<Set<PersonalChatMessageItem>>(emptySet()) }
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
    ) {

        items(
            messageWithMediaList.size)
        { index ->
            val messageItem = messageWithMediaList[index]
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
            )
            {
                if (totalDragDistance > 0f) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp)
                    ) {
                        if (isReplyTriggered) {
                            Icon(
                                painter = painterResource(id = R.drawable.reply_icon),
                                contentDescription = "Reply",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                val replyMessageItem = PersonalChatRoomUtil.findMessageById(
                    messageId = messageItem.replyMessageId,
                    messageWithMediaList = messageWithMediaList
                )
                when (messageItem) {
                    is PersonalChatMessageItem.PSenderTextChatItem -> SenderTextChatItemCard(
                        item = messageItem,
                        isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PSenderImageChatItem -> SenderImageChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PSenderPdfChatItem -> SenderPdfChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PSenderExcelDocChatItem -> SenderExcelDocChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PSenderWordDocChatItem -> SenderWordDocChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PSenderAutoCadChatItem -> SenderAutoCadChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PReceiverTextChatItem -> ReceiverTextChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PReceiverImageChatItem -> ReceiverImageChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PReceiverPdfChatItem -> ReceiverPdfChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PReceiverExcelDocChatItem -> ReceiverExcelDocChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PReceiverWordDocChatItem -> ReceiverWordDocChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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

                    is PersonalChatMessageItem.PReceiverAutoCadChatItem -> ReceiverAutoCadChatItemCard(
                        messageItem, isSelected = selectedMessageList.contains(messageItem),
                        replyMessageItem = replyMessageItem,
                        onReplySectionClick = {
                            PersonalChatRoomUtil.handleReply(
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