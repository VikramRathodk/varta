package com.devvikram.varta.ui.screens.conversations

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingAccctionButton(openContactsList: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.padding(16.dp),
        onClick = { openContactsList() }
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add Contact")
    }

}