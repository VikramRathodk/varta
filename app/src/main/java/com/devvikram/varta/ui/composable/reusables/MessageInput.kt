package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem


@Composable
fun <T> MessageInput(
    modifier: Modifier = Modifier,
    replyMessageItem : T,
    closeReplyView : (Boolean) -> Unit,
    isReplyMode : Boolean = false,
    onMessageSend: (String) -> Unit,
    onCameraClick: () -> Unit,
    onAttachmentClick: () -> Unit,
    isCameraEnabled: Boolean = true,
    isAttachmentEnabled: Boolean = true,
    hintMessage: String = "Type a Message..."
) {
    val message = remember { mutableStateOf("") }
    val isCameraVisible = remember { mutableStateOf(true) }
    val replySenderName = when (replyMessageItem) {
        is PersonalChatMessageItem -> replyMessageItem.senderName
        is GroupChatMessageItem -> replyMessageItem.senderName
        else -> ""
    }
    val isDarkMode = isSystemInDarkTheme()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Reply section
            if(isReplyMode){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 8.dp
                        )
                        .background(
                            color = if (isDarkMode) Color.DarkGray.copy(
                                alpha = 0.2f
                            ) else {
                                Color.LightGray.copy(
                                    alpha = 0.2f
                                )
                            },
                            shape = RoundedCornerShape(12.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                horizontal = 4.dp,
                                vertical = 8.dp
                            )
                    ) {
                        Text(
                            text = "Replying to $replySenderName",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "This is the message preview...",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(model = "", error = painterResource(id = R.drawable.image_icon)),
                        contentDescription = "File Preview",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp),
                        colorFilter = ColorFilter.tint(if (isDarkMode) Color.White else Color.Black)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel reply",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                            .clickable {
                                closeReplyView(true)
                            },
                        colorFilter = ColorFilter.tint(if (isDarkMode) Color.White else Color.Black)

                    )

                }
                Divider(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }else{
                println(
                    "No reply message provided. Showing the default input field."
                )
            }

            // Message input section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.2f
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                // Camera Icon
                if(isCameraEnabled){
                    if (isCameraVisible.value) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                            contentDescription = "Open camera to capture photo or video",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(2.dp)
                                .clickable { onCameraClick() },
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }


                // Message Input Field
                OutlinedTextField(
                    value = message.value,
                    onValueChange = {
                        message.value = it
                        isCameraVisible.value = it.isBlank()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Send
                    ),
                    maxLines = 5,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = false,
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (message.value.isNotBlank()) {
                                onMessageSend(message.value)
                                message.value = ""
                            }
                        }
                    ),
                    placeholder = {
                        Text(
                            text = hintMessage,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Attachment Icon
                if(isAttachmentEnabled){
                    Image(
                        painter = painterResource(id = R.drawable.baseline_attachment_24),
                        contentDescription = "Attach a file",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .clickable { onAttachmentClick() },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }


                Spacer(modifier = Modifier.width(8.dp))

                // Send Icon
                Image(
                    painter = painterResource(id = R.drawable.send_icon),
                    contentDescription = "Send message",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .clickable(enabled = message.value.isNotBlank()) {
                            onMessageSend(message.value)
                            message.value = ""
                        },
                    colorFilter = ColorFilter.tint(if (isDarkMode) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }



}
