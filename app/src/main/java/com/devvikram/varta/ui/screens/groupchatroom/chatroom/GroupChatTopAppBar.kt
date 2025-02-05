package com.devvikram.varta.ui.screens.groupchatroom.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.composable.reusables.ReusableDropdownMenu
import com.devvikram.varta.ui.itemmodels.MenuItemsModel
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatTopAppBar(
    modifier: Modifier,
    navHostController: NavHostController,
    groupChatRoomViewmodel: GroupChatRoomViewmodel,
    onNavigationClick: () -> Unit,
    mainGroupChatRoomViewModel: MainGroupChatRoomViewModel,
) {
    var showMenu by remember { mutableStateOf(false) }
    val menuItems = remember {
        mutableListOf(
            MenuItemsModel(title = "Search", icon = Icons.Default.Person),
            MenuItemsModel(title = "Settings", icon = Icons.Default.Settings),
        )
    }

    val groupMembers = mainGroupChatRoomViewModel.groupMembers.collectAsState()
    val groupInformation = mainGroupChatRoomViewModel.groupInformation.collectAsState()


    TopAppBar(
        modifier = modifier.clickable {
            navHostController.navigate(
                Destination.GroupProfileInfo(groupChatRoomViewmodel.currentConversationId.value)
            )
        },
        navigationIcon = {
            BackNavigationButton {
                onNavigationClick()
            }
        },
        title = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.group_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Column(
                    modifier = modifier.padding(start = 8.dp)
                ) {
                    groupInformation.value?.let {
                        Text(
                            text = it.groupName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Text(
                        text = "${groupMembers.value.size} participants",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start,
                        )
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { showMenu = !showMenu },
                modifier = Modifier.clip(
                    shape = CircleShape
                )
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                )
            }
            ReusableDropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                items = menuItems,
                onItemClick = {
                    println("Selected group chat item: $it")
                }
            )

        }
    )
}