package com.devvikram.varta.ui.composable.menus

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GroupMemberPopUpOptionMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    isAdmin: Boolean,
    onRemoveMember: () -> Unit,
    onMakeAdmin: () -> Unit,
    onRemoveAdmin: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
    ) {
        if (isAdmin) {
            DropdownMenuItem(
                text = { Text("Remove Admin", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Admin", tint = MaterialTheme.colorScheme.error)
                },
                onClick = {
                    onRemoveAdmin()
                    onDismissRequest()
                }
            )
        } else {
            DropdownMenuItem(
                text = { Text("Make Admin", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = {
                    Icon(Icons.Default.Star, contentDescription = "Make Admin", tint = MaterialTheme.colorScheme.primary)
                },
                onClick = {
                    onMakeAdmin()
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text("Remove Member", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Member", tint = MaterialTheme.colorScheme.error)
                },
                onClick = {
                    onRemoveMember()
                    onDismissRequest()
                }
            )
        }
    }
}



