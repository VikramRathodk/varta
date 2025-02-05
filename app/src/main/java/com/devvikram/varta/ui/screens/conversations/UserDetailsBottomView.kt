package com.devvikram.varta.ui.screens.conversations

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.ui.composable.reusables.ProfileBottomSheet

@Composable
fun UserDetailsBottomView(
    modifier: Modifier,
    isDarkMode: Boolean = false,
    currentLoggedUser: ProContacts?,
    onLogout: () -> Unit = {}
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ProfileBottomSheet(
            showBottomSheet = true,
            currentLoggedUser = currentLoggedUser,
            onDismiss = { showBottomSheet = false },
            onLogout = onLogout
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < 0) {
                        showBottomSheet = true
                    }
                }
            }
            .clickable {
                showBottomSheet = true
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.7f,),
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.7f,
            )
        ),
        shape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentLoggedUser?.name ?: "NA",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = if(isDarkMode) FontWeight.SemiBold else FontWeight.Bold,
                    color = if(isDarkMode) Color.White else MaterialTheme.colorScheme.onPrimary
                ),
                modifier = modifier.weight(1f)
            )

            Image(
                painter = painterResource(id = R.drawable.varta_logo),
                contentDescription = "User profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

    }
}