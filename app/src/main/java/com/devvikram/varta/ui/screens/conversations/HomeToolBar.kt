package com.devvikram.varta.ui.screens.conversations

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.ui.itemmodels.MenuItemsModel
import com.devvikram.varta.ui.composable.reusables.ReusableDropdownMenu
import com.devvikram.varta.ui.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeToolBar(
    modifier: Modifier,
    onSearchIconClick: () -> Unit,
    homeViewModel: HomeViewModel
) {
    var mDisplayMenu by remember { mutableStateOf(false) }
    val menuItems = remember { mutableListOf(MenuItemsModel(title = "Logout", icon = Icons.Default.ExitToApp),) }
    TopAppBar(
        title = {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.varta_logo),
                    modifier = Modifier.size(48.dp).clip(
                        shape = CircleShape
                    ),
                    contentDescription = "User profile picture",
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onSearchIconClick()
            }) {
                Icon(Icons.Default.Search, "")
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
                        "Logout" -> {
                            homeViewModel.setIsLogoutVisible(true)
                            mDisplayMenu = false
                        }
                    }
                }
            )

        }
    )
}