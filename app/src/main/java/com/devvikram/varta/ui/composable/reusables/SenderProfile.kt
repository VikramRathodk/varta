package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R


@Composable
fun SenderProfile(
    profileImageUrl: String,
    placeholderResId: Int = R.drawable.baseline_person_24,
    size: Dp = 32.dp
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = profileImageUrl,
            error = painterResource(id = placeholderResId)
        ),
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
        contentDescription = "Sender Profile",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
            .padding(8.dp)
    )
}
