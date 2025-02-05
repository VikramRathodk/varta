package com.devvikram.varta.ui.composable.reusables.texts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextMessageWarning(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium
    )
}