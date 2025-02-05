package com.devvikram.varta.ui.composable.reusables

import androidx.compose.runtime.Composable
import com.devvikram.varta.R
import com.devvikram.varta.ui.screens.messageinformation.UserCardWithTime

@Composable
fun <T>SentTimeCard(selectedMessageItem: T?) {
    MessageTypeHeaderCard(type = "Sent", icon = R.drawable.message_sent_icon)
    UserCardWithTime(selectedMessageItem)
}