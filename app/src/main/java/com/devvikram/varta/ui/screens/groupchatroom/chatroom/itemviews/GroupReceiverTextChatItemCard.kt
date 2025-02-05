package com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devvikram.varta.R
import com.devvikram.varta.ui.composable.reusables.ReplyLayout
import com.devvikram.varta.ui.composable.reusables.SenderProfile
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem

@Composable
fun GroupReceiverTextChatItemCard(
    item: GroupChatMessageItem.GReceiverTextChatItem, isSelected: Boolean,
    replyMessageItem: GroupChatMessageItem?,
    onReplySectionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else Color.Transparent
            ), horizontalArrangement = Arrangement.Start
    ) {

        SenderProfile(
            profileImageUrl = item.senderName,
            size = 32.dp
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .border(
                    border = BorderStroke(
                        width = 0.1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .background(
                    color = if (isSystemInDarkTheme()) Color.DarkGray.copy(
                        alpha = 0.1f
                    ) else Color.White.copy(
                        alpha = 0.8f
                    ),
                    shape = RoundedCornerShape(
                        topStart = 0.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp
                    )
                )
                .padding(8.dp)
        ) {
            Text(
                text = "Suraj Anbhule",
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            if (replyMessageItem != null) {
                ReplyLayout(
                    content = replyMessageItem.content,
                    fileName = replyMessageItem.fileName,
                    isMedia = replyMessageItem.isMediaMessage,
                    mediaType = replyMessageItem.mediaType,
                    onReplySectionClicked = onReplySectionClick
                )
            }

            // Message Text
            Text(
                text = item.content,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(
                        max = 200.dp
                    )
            )

            // Timestamp and Starred Message
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.End)
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_star_24_filled),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                )
                Text(
                    text = item.timestamp.substring(
                        item.timestamp.length - 5, item.timestamp.length
                    ),
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}