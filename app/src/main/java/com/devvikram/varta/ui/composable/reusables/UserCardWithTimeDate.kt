package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T>UserCardWithTimeDate(
    selectedMessageItem: T,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(width = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(
            alpha = 0.5f
        )),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            ProfileImage(
                imageUrl = "",
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "Vikram Rathod",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "12:00 PM",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}