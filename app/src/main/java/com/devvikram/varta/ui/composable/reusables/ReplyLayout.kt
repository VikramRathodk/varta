package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devvikram.varta.R

@Composable
fun ReplyLayout(
    fileName: String,
    content: String,
    isMedia: Boolean,
    mediaType: String,
    onReplySectionClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .widthIn(min = 100.dp, max = 250.dp)
            .clickable { onReplySectionClicked() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            // Vertical divider
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(28.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            // Reply message and file name container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = content,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 3,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (fileName.isNotEmpty()) {
                    Text(
                        text = fileName,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Light
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            // Reply image or media
            if (isMedia) {
                val icon = when (mediaType) {
                    "image" -> R.drawable.image_icon
                    "video" -> R.drawable.video_icon
                    else -> R.drawable.document_file_icon
                }
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Reply placeholder",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                        .padding(6.dp)
                )
            }
        }
    }
}


