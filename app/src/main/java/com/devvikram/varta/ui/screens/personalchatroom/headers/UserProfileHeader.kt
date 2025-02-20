package com.devvikram.varta.ui.screens.personalchatroom.headers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.screens.personalchatroom.PersonalChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileHeader(
    personalChatViewModel: PersonalChatViewModel,
    currentReceiver: ProContacts?,
    onClick: () -> Unit,
    onBackPressed: () -> Unit
) {

    var mDisplayMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier
            .wrapContentHeight()
            .clickable {
                onClick()
            },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surface
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        currentReceiver?.localProfilePicPath,
                        error = painterResource(id = R.drawable.baseline_person_24)
                    ),
                    contentDescription = "User profile picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .padding(end = 16.dp)
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = currentReceiver?.name ?: "",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = currentReceiver?.statusText ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start,
                        )
                    )
                }
            }
        }, navigationIcon = {
            BackNavigationButton {
                onBackPressed()
            }
        },
        actions = {
            IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                Icon(Icons.Default.MoreVert, "")
            }
            DropdownMenu(
                expanded = mDisplayMenu,
                onDismissRequest = { mDisplayMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Search") },
                    onClick = {

                    }
                )
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        mDisplayMenu = false
                    }
                )
            }

        }
    )


}