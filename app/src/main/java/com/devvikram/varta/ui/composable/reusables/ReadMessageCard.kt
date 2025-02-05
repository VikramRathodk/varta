package com.devvikram.varta.ui.composable.reusables

import androidx.compose.runtime.Composable
import com.devvikram.varta.R
import com.devvikram.varta.ui.screens.messageinformation.UserCardWithTime

@Composable
fun <T>ReadMessageCard(
    selectedMessageItem: T?,
) {
    MessageTypeHeaderCard(type = "Read", icon = R.drawable.message_seen_icon)
    UserCardWithTime(selectedMessageItem)
}