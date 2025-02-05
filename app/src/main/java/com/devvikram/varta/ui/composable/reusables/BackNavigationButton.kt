package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun BackNavigationButton(onClick : () -> Unit) {
    IconButton(
        onClick = {
            onClick()
        }, modifier = Modifier.clip(
            shape = CircleShape
        )
    ) {
        Icon(Icons.Filled.ArrowBack, contentDescription = null)
    }
}
