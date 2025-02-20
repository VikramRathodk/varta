package com.devvikram.varta.ui.screens.groupchatroom.addnewmembers

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.composable.reusables.MySearchBar
import com.devvikram.varta.ui.composable.reusables.ProfileImage
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupContactItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewMemberScreen(
    navHostController: NavHostController,
    viewModel: GroupAddNewViewModel,
    conversationId : String
) {
    val contactItems = viewModel.contactItems.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val selectedContactItems = viewModel.selectedContactItems.collectAsState(initial = emptyList())
    val searchQuery = viewModel.searchQuery.collectAsState()
    LaunchedEffect (
        key1 = contactItems.value,
        key2 = selectedContactItems.value,
        key3 = searchQuery.value
    ){
        viewModel.updateConversationId(conversationId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Add New Member") },
                navigationIcon = {
                    BackNavigationButton(
                        onClick = { navHostController.popBackStack() }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            MySearchBar(
                onBackPressed = {
                    navHostController.popBackStack()
                },
                placeholder = "Search members...",
                query =searchQuery.value,
                onQueryChanged = {
                    viewModel.updateSearchQuery(it)
                },
                isBackNavigationEnabled = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(contactItems.value) { contactItem ->
                    when (contactItem) {
                        is GroupContactItem.SelectedGroupContactItem -> {
                            SelectedGroupContactItemView(
                                item = contactItem,
                                onToggleSelection = {
                                    viewModel.toggleSelection(contactItem)
                                }
                            )
                        }

                        is GroupContactItem.UnselectedGroupContactItem -> {
                            UnselectedGroupContactItemView(
                                item = contactItem,
                                onToggleSelection = {
                                    viewModel.toggleSelection(contactItem)
                                }
                            )
                        }

                        is GroupContactItem.AlreadyAddedGroupContactItem -> {
                            AlreadyAddedGroupContactItemView(item = contactItem)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                enabled = selectedContactItems.value.isNotEmpty(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedContactItems.value.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (selectedContactItems.value.isNotEmpty()) Color.White else MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = if (selectedContactItems.value.isNotEmpty()) MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.2f
                    ) else Color.Transparent,
                    disabledContentColor = if (selectedContactItems.value.isNotEmpty()) MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    ) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                onClick = {
                    if (viewModel.selectedContactItems.value.isNotEmpty()) {
                        viewModel.addSelectedMembersToGroup(conversationId = conversationId )
                       Toast.makeText(
                            context,
                            "Members added successfully to group.",
                            Toast.LENGTH_SHORT
                        ).show(
                       )
                        navHostController.popBackStack()
                    } else {
                        Toast.makeText(
                            context,
                            "Please select members to add to group.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
            ) {
                Text(text = "Add Selected Members")
            }
        }
    }
}

@Composable
fun AlreadyAddedGroupContactItemView(item: GroupContactItem.AlreadyAddedGroupContactItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(imageUrl = item.localProfilePath)
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.proNameOfUser,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun SelectedGroupContactItemView(
    item: GroupContactItem.SelectedGroupContactItem,
    onToggleSelection: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onToggleSelection() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(imageUrl = item.localProfilePath)
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.proNameOfUser,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun UnselectedGroupContactItemView(
    item: GroupContactItem.UnselectedGroupContactItem,
    onToggleSelection: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onToggleSelection() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(imageUrl = item.localProfilePath)
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.proNameOfUser,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

