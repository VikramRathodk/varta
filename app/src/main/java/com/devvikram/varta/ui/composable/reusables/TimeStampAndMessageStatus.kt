package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TimeStampAndMessageStatus(
    isFavorite: Boolean,
    timestamp: String,
    messageStatus: String,
    modifier: Modifier = Modifier
) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.End,
//        modifier = modifier
//            .wrapContentWidth()
//
//    ) {
//        if (isFavorite) {
//            Icon(
//                painter = painterResource(id = R.drawable.baseline_star_24_filled),
//                contentDescription = "Starred Message",
//                modifier = Modifier
//                    .size(16.dp)
//                    .padding(end = 4.dp),
//                tint = MaterialTheme.colorScheme.secondary
//            )
//        }
//
//        Text(
//            text = timestamp.substring(11, 16),
//            style = TextStyle(
//                color = MaterialTheme.colorScheme.onTertiaryContainer,
//                fontSize = 12.sp
//            ),
//            modifier = Modifier.padding(end = 4.dp)
//        )
//
//        // Message Status
//        when (messageStatus) {
//            "sent" -> {
//                Icon(
//                    painter = painterResource(id = R.drawable.message_sent_icon),
//                    contentDescription = "Message Sent",
//                    modifier = Modifier.size(16.dp),
//                    tint = MaterialTheme.colorScheme.onTertiaryContainer
//                )
//            }
//
//            "delivered" -> {
//                Icon(
//                    painter = painterResource(id = R.drawable.message_delivered_icon),
//                    contentDescription = "Message Delivered",
//                    modifier = Modifier.size(16.dp),
//                    tint = MaterialTheme.colorScheme.onTertiaryContainer
//                )
//            }
//
//            "read" -> {
//                Icon(
//                    painter = painterResource(id = R.drawable.message_seen_icon),
//                    contentDescription = "Message Read",
//                    modifier = Modifier.size(16.dp),
//                    tint = MaterialTheme.colorScheme.onTertiaryContainer
//                )
//            }
//        }
//    }

}
