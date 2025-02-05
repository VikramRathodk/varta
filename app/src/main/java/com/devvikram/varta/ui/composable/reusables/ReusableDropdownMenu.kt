package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devvikram.varta.ui.itemmodels.MenuItemsModel

@Composable
fun ReusableDropdownMenu(
    items: List<MenuItemsModel>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: (MenuItemsModel) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .clip(MaterialTheme.shapes.medium)
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                onClick = {
                    onItemClick(item)
                    onDismissRequest()
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReusableDropdownMenu() {
    val menuItems = listOf(
        MenuItemsModel("Profile", Icons.Default.Person),
        MenuItemsModel("Settings", Icons.Default.Settings),
        MenuItemsModel("Logout", Icons.Default.ExitToApp)
    )
    ReusableDropdownMenu(
        items = menuItems,
        expanded = true,
        onDismissRequest = {},
        onItemClick = {}
    )
}

