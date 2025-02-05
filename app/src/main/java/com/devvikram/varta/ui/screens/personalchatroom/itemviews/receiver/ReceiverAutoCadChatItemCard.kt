package com.devvikram.varta.ui.screens.personalchatroom.itemviews.receiver

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.alpha
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
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem
import com.devvikram.varta.utility.DownloadState
import com.devvikram.varta.utility.simulateDownload


@Composable
fun ReceiverAutoCadChatItemCard(
    item: PersonalChatMessageItem.PReceiverAutoCadChatItem,
    isSelected: Boolean,
    onReplySectionClick: () -> Unit,
    replyMessageItem: PersonalChatMessageItem? = null
) {
    val downloadState = remember { mutableStateOf(DownloadState.Initial) }
    val alpha = if (downloadState.value!= DownloadState.Completed) 0.5f else 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else Color.Transparent
            ),
        horizontalArrangement = Arrangement.Start
    ) {
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
                    color = if(isSystemInDarkTheme()) Color.DarkGray.copy(
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
                if (replyMessageItem != null) {
                    ReplyLayout(
                        content = replyMessageItem.content,
                        fileName = replyMessageItem.filename,
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
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.document_file_icon),
                        contentDescription = "PDF File",
                        modifier = Modifier
                            .size(28.dp)
                            .alpha(alpha),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (item.filename.length > 15) {
                            "${item.filename.take(15)}..."
                        } else {
                            item.filename
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Normal
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .alpha(alpha)
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
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .wrapContentWidth()
                        .widthIn(
                            max = 200.dp
                        )
                        .padding(top = 8.dp)
                )

                // row of star , timestamp and status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
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
                            color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
    }
}
