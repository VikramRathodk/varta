package com.devvikram.varta.ui.screens.groupchatroom.chatroom.itemviews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.ui.composable.reusables.DownloadStateIcon
import com.devvikram.varta.ui.composable.reusables.ReplyLayout
import com.devvikram.varta.ui.composable.reusables.SenderProfile
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import com.devvikram.varta.utility.DownloadState
import com.devvikram.varta.utility.simulateDownload

@Composable
fun GroupReceiverImageChatItemCard(
    item: GroupChatMessageItem.GReceiverImageChatItem, isSelected: Boolean,
    replyMessageItem: GroupChatMessageItem?,
    onReplySectionClick: () -> Unit,
) {
    val downloadState = remember { mutableStateOf(DownloadState.Initial) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else Color.Transparent
            ),
        horizontalArrangement = Arrangement.Start
    ) {
        SenderProfile(
            profileImageUrl = item.senderName,
            size = 32.dp
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
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

            Column(
                modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Suraj Anbhule",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
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

                // Image with constrained size
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = item.imageUrl,
                            error = painterResource(id = R.drawable.image_icon)
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (16).dp, y = (16).dp)
                            .padding(vertical = 16.dp, horizontal = 16.dp)
                    ) {
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

                }
                // content
                Text(
                    text = item.content,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,

                        fontSize = 15.sp,
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .wrapContentWidth()
                        .widthIn(
                            max = 200.dp
                        )
                        .padding(top = 8.dp)
                )


                // Timestamp, star, and status icons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                                .size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = item.timestamp.substring(11, 16),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontSize = 12.sp
                        ),
                        textAlign = TextAlign.End
                    )
                }

            }
        }
    }
}