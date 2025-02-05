package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.data.room.models.ProContacts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheet(
    showBottomSheet: Boolean,
    currentLoggedUser :ProContacts?,
    onDismiss: () -> Unit,
    onLogout: () -> Unit = {}
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor =MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
            tonalElevation = 16.dp,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
        ) {
            val profileImage = painterResource(id = R.drawable.baseline_person_24)
            ProfileLayout(
                currentLoggedUser = currentLoggedUser,
                onLogout = onLogout
            )
        }
    }
}
@Composable
fun ProfileLayout(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    currentLoggedUser: ProContacts?
) {
    val profileItems = listOf(
        "View Profile" to R.drawable.baseline_person_24,
        "Saved Messages" to R.drawable.baseline_save_24,
        "Settings" to R.drawable.baseline_settings_24,
        "Create New Group" to R.drawable.baseline_group_add_24
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Transparent)
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .border(
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile Image
                Image(
                    painter = rememberAsyncImagePainter(currentLoggedUser?.localProfilePicPath),
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                        Text(
                            text = currentLoggedUser?.name ?: "NA",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                    Text(
                        text = currentLoggedUser?.designation ?: "NA",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Logout Button
                IconButton(
                    onClick = { onLogout() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Profile Items
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp),
//            verticalArrangement = Arrangement.spacedBy(4.dp),
//        ) {
//            items(profileItems) { (text, iconRes) ->
//                ProfileVerticalCard(text = text, iconRes = iconRes)
//            }
//        }
    }
}

@Composable
fun ProfileVerticalCard(
    text: String,
    iconRes: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .height(60.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { /* Handle click */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Text
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


