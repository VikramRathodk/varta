package com.devvikram.varta.ui.screens.personalchatroom.itemviews.sender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devvikram.varta.R
import com.devvikram.varta.ui.composable.reusables.ReplyLayout
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem

@Composable
fun SenderTextChatItemCard(
    item: PersonalChatMessageItem.PSenderTextChatItem,
    isSelected: Boolean,
    onReplySectionClick: () -> Unit,
    replyMessageItem: PersonalChatMessageItem?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else Color.Transparent
            ),

        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = if (isSelected) 1.0f else 0.6f
                    ), shape = RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .wrapContentWidth()
        ) {
            Column(
                modifier = Modifier.wrapContentWidth()
            ) {

                // reply section

                if (replyMessageItem != null) {
                    ReplyLayout(
                        content = replyMessageItem.content,
                        fileName = replyMessageItem.filename,
                        isMedia = replyMessageItem.isMediaMessage,
                        mediaType = replyMessageItem.mediaType,
                        onReplySectionClicked = onReplySectionClick
                    )
                }


                // "Forwarded" indicator
                if (item.isForwared) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.forward_icon_16),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = "Forwarded",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                }

                // Message content
                Text(
                    text = item.content,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .widthIn(
                            max = 200.dp
                        )
                )

                // Timestamp and message status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.End)
                ) {
                    if (item.isFavorite) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_star_24_filled),
                            contentDescription = "Starred Message",
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 4.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = item.timestamp,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    // Message Status
                    when (item.messageStatus) {
                        "sent" -> {
                            Icon(
                                painter = painterResource(id = R.drawable.message_sent_icon),
                                contentDescription = "Message Sent",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }

                        "delivered" -> {
                            Icon(
                                painter = painterResource(id = R.drawable.message_delivered_icon),
                                contentDescription = "Message Delivered",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }

                        "read" -> {
                            Icon(
                                painter = painterResource(id = R.drawable.message_seen_icon),
                                contentDescription = "Message Read",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}