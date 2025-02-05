package com.devvikram.varta.ui.screens.groupchatroom.commoncomposable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.ui.composable.menus.GroupMemberPopUpOptionMenu
import com.devvikram.varta.ui.composable.reusables.chips.TagChipButton
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomViewModel

@Composable
fun GroupAdminRow( userId: String,
                   name: String, designation: String,
                   profileImage: String,
                   mainGroupChatRoomViewModel: MainGroupChatRoomViewModel
) {
    var menuVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { menuVisible = true }
                )
            }
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.08f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.varta_logo),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
            alignment = Alignment.CenterStart
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
        TagChipButton(text = "Admin", onClick = { })


        // Option Menu
        GroupMemberPopUpOptionMenu(
            expanded = menuVisible,
            onDismissRequest = { menuVisible = false },
            isAdmin = true,
            onRemoveMember = {
                mainGroupChatRoomViewModel.removeMember(userId)
            },
            onMakeAdmin = {
                mainGroupChatRoomViewModel.makeAdmin(userId)
            },
            onRemoveAdmin = {
                mainGroupChatRoomViewModel.removeAdmin(userId)
            }
        )
    }
}
