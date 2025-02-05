package com.devvikram.varta.ui.screens.conversations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.ui.itemmodels.ConversationItem

@Composable
fun PersonalConversationItemView(
    personalConversationItem: ConversationItem.PersonalConversation,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                MaterialTheme.colorScheme.background.copy(alpha = 0.1f)
            )
            .clip(
                RoundedCornerShape(
                    topStart = 4.dp, topEnd = 4.dp, bottomStart = 0.dp, bottomEnd = 0.dp
                )
            )
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //   User Profile Picture
        Image(
            painter = rememberAsyncImagePainter(
                personalConversationItem.localProfilePicPath, error = painterResource(
                    id = R.drawable.baseline_person_24
                )
            ),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .border(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // User Name and Last Message
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            // User Name
            Text(
                text = personalConversationItem.proContactName,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Message Status Icon (Sent/Delivered)
                when (personalConversationItem.lastMessageStatus) {
                    "sent" -> Icon(
                        painter = painterResource(id = R.drawable.message_sent_icon),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    "delivered" -> Icon(
                        painter = painterResource(id = R.drawable.message_delivered_icon),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Media Message Icon (Image, Video, Audio, PDF, etc.)
                if (personalConversationItem.isMediaMessage) {
                    when (personalConversationItem.mediaType) {
                        "image" -> Icon(
                            painter = painterResource(id = R.drawable.image_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        "video" -> Icon(
                            painter = painterResource(id = R.drawable.video_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        "audio" -> Icon(
                            painter = painterResource(id = R.drawable.document_file_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        "pdf" -> Icon(
                            painter = painterResource(id = R.drawable.document_file_icon),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Last Message Text
                Text(
                    text = "You have successfully created a new document.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Last message time and  Unread Message Count
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (personalConversationItem.unreadMessageCount > 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.3f
                        ), CircleShape
                        )
                ) {
                    Text(
                        text = personalConversationItem.unreadMessageCount.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
    }
}