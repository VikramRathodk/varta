package com.devvikram.varta.ui.screens.groupchatroom.chatroom

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.ui.composable.reusables.ReusableDropdownMenu
import com.devvikram.varta.ui.itemmodels.MenuItemsModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionModeTopBar(
    selectedCount: Int,
    onCloseSelection: () -> Unit,
    onForward: () -> Unit,
    onReply: () -> Unit,
    onCopy: () -> Unit,
    onInfo: () -> Unit,
    onUnselectionAll: () -> Unit,
    onDeactivateReplyMode: () -> Unit = {}
) {

    var mDisplayMenu by remember { mutableStateOf(false) }

    val menuItems = listOf(
        MenuItemsModel("Info", Icons.Default.Info),
        MenuItemsModel("Unselect All", Icons.Default.Close)
    )

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onCloseSelection) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close selection"
                )
            }
        },
        title = { Text("$selectedCount selected") },
        actions = {
            if (selectedCount <= 1) {
                IconButton(onClick = onReply) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.reply_icon),
                        contentDescription = "Reply"
                    )
                }
            }else{
                onDeactivateReplyMode()
            }

            IconButton(onClick = onCopy) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.copy_icon),
                    contentDescription = "Copy"
                )
            }
            IconButton(onClick = onForward) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.forward_icon),
                    contentDescription = "Forward"
                )
            }
            IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                Icon(Icons.Default.MoreVert, "")
            }

            ReusableDropdownMenu(
                expanded = mDisplayMenu,
                onDismissRequest = { mDisplayMenu = false },
                items = menuItems,
                onItemClick = {
                    when (it.title) {
                        "Info" -> onInfo()
                        "Unselect All" -> onUnselectionAll()
                    }
                }
            )
        }
    )
}