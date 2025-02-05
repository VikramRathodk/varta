package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R

@Composable
fun ProfileImage(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            error = painterResource(id = R.drawable.baseline_person_24)
        ),
        contentDescription = "Profile Picture",
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            )
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                shape = CircleShape
            ),
        contentScale = ContentScale.Crop
    )
}