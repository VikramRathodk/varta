package com.devvikram.varta.ui.screens.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.ui.itemmodels.ContactMultipleItem

@Composable
fun SingleContactCard(
    contactMultipleItem: ContactMultipleItem.ContactItem,
    onLongPressClicked: () -> Unit,
    onSingleClick: () -> Unit = {},
    isSelected: Boolean = false
) {
    val backgroundColor =
        if (isSelected) colorScheme.primary.copy(alpha = 0.2f) else colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPressClicked()
                    },
                    onTap = {
                        onSingleClick()
                    }
                )
            }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clip(MaterialTheme.shapes.medium),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = contactMultipleItem.contact.localProfilePicPath,
                    error = painterResource(id = R.drawable.baseline_person_24)
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.dp, colorScheme.primary, CircleShape)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = contactMultipleItem.contact.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = contactMultipleItem.contact.designation ?: "|NA|",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Modifier.clickable(onClick = onSingleClick)
    }
}