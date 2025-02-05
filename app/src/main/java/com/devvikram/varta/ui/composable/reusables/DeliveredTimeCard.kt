package com.devvikram.varta.ui.composable.reusables

import androidx.compose.runtime.Composable
import com.devvikram.varta.R
import com.devvikram.varta.ui.screens.messageinformation.UserCardWithTime

@Composable
fun <T>DeliveredTimeCard(
    selectedMessageItem: T?,
) {
    MessageTypeHeaderCard(type = "Delivered", icon = R.drawable.message_delivered_icon)
    UserCardWithTime(selectedMessageItem)
}
