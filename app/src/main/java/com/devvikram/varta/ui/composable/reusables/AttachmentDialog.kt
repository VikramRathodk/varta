package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.ui.itemmodels.AttachmentOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentDialog(onDismiss: () -> Unit,
                     onOptionSelected: (String) -> Unit) {
    val attachmentOptions = listOf(
        AttachmentOption("Document", R.drawable.document_file_icon, MaterialTheme.colorScheme.primaryContainer),
        AttachmentOption("Camera", R.drawable.baseline_photo_camera_24, MaterialTheme.colorScheme.secondaryContainer),
        AttachmentOption("Gallery", R.drawable.gallary_icon, MaterialTheme.colorScheme.tertiaryContainer),
        AttachmentOption("Audio", R.drawable.headphones_icon, MaterialTheme.colorScheme.errorContainer),
        AttachmentOption("Location", R.drawable.location_icon, MaterialTheme.colorScheme.secondaryContainer)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(attachmentOptions) { option ->
                    AttachmentItem(option) { optionSelected ->
                        println("Option selected: ${optionSelected.name}")
                        onOptionSelected(optionSelected.name)
                        onDismiss()
                    }
                }
            }

    }
}

@Composable
fun AttachmentItem(option: AttachmentOption, onClick: (AttachmentOption) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(option) }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Elevated icon with shadow
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(option.color.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = option.icon),
                contentDescription = option.name,
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = option.name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 1
        )
    }
}
