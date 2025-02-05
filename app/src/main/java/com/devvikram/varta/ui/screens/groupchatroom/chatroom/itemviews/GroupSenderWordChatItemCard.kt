package com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devvikram.varta.R
import com.devvikram.varta.ui.composable.reusables.DownloadStateIcon
import com.devvikram.varta.ui.composable.reusables.ReplyLayout
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import com.devvikram.varta.utility.DownloadState
import com.devvikram.varta.utility.simulateDownload

@Composable
fun GroupSenderWordChatItemCard(
    item: GroupChatMessageItem.GSenderWordChatItem, isSelected: Boolean,
    replyMessageItem: GroupChatMessageItem?,
    onReplySectionClick: () -> Unit,
) {
    val downloadState = remember { mutableStateOf(DownloadState.Initial) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else Color.Transparent
            ),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 0.dp
                    )
                )
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.End
            ) {
                if (replyMessageItem != null) {
                    ReplyLayout(
                        content = replyMessageItem.content,
                        fileName = replyMessageItem.fileName,
                        isMedia = replyMessageItem.isMediaMessage,
                        mediaType = replyMessageItem.mediaType,
                        onReplySectionClicked = onReplySectionClick
                    )
                }
                // PDF File Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .widthIn(
                            min = 180.dp,
                            max = 250.dp
                        )
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        )
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.document_file_icon),
                        contentDescription = "PDF File",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (item.fileName.length > 15) {
                            "${item.fileName.take(15)}..."
                        } else {
                            item.fileName
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DownloadStateIcon(
                        downloadState = downloadState.value,
                        onDownloadClick = {
                            downloadState.value = DownloadState.Downloading
                            simulateDownload { success ->
                                downloadState.value = if (success) {
                                    DownloadState.Completed
                                } else {
                                    DownloadState.Error
                                }
                            }
                        },
                        onRetryDownload = {
                            downloadState.value = DownloadState.Downloading
                            simulateDownload { success ->
                                downloadState.value = if (success) {
                                    DownloadState.Completed
                                } else {
                                    DownloadState.Error
                                }
                            }
                        }
                    )
                }
                // content
                Text(
                    text = item.content,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,

                        fontSize = 15.sp,
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // row of star , timestamp and status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                ) {
                    if (item.isFavorite) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_star_24_filled),
                            contentDescription = "Starred Message",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 4.dp, end = 4.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = item.timestamp.substring(
                            11, 16
                        ), style = TextStyle(
                            color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(end = 4.dp)
                    )

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