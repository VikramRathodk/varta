package com.devvikram.varta.ui.screens.contacts

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.ui.composable.reusables.headerrow.HeaderRow
import com.devvikram.varta.ui.itemmodels.ContactMultipleItem
import com.devvikram.varta.ui.screens.creategroup.CreateGroupDialog
import com.devvikram.varta.ui.screens.creategroup.CreateGroupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    navHostController: NavHostController,
    contactViewModel: ContactViewModel,
    groupCreateViewModel: CreateGroupViewModel,
    onBackClick: () -> Boolean,

) {

    val contacts by contactViewModel.contacts.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val items = remember { mutableStateListOf<ContactMultipleItem>() }
    val loginPreference = LoginPreference(context)

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(key1 = contacts) {
        Log.d("DEBUG", "Contacts updated: ${contacts}")
        if (contacts.isNotEmpty()) {
            val contactItems = contacts.filter { it.userId.toString() != loginPreference.getUserId() }.map {
                ContactMultipleItem.ContactItem(it)
            }
            items.clear()
            items.addAll(contactItems)
        }
    }

    val selectedContacts = remember { mutableStateListOf<ContactMultipleItem.ContactItem>() }
    val longPressActive = remember { mutableStateOf(false) }
    val isOpenGroupCreateDialog = remember { mutableStateOf(false) }

    Surface{
        Column(
            modifier = Modifier
               .fillMaxSize()
        ) {

            ContactToolBar(
                onBackClick = onBackClick,
                selectedContacts = selectedContacts,
                isOpenGroupCreateDialog,
                longPressActive
            )

            LazyColumn(
                modifier = Modifier.padding(vertical = 4.dp).weight(1f),
            ) {
                item {
                    HeaderRow(
                        title =  "Create New Group ",
                        icon = R.drawable.baseline_group_add_24,
                        iconSize = 40.dp,
                        onClickListener = { navHostController.navigate(Destination.CreateGroup)}
                    )
                }
                items(items.size) { index ->
                    when (val item = items[index]) {
                        is ContactMultipleItem.ContactItem -> {
                            SingleContactCard(
                                contactMultipleItem = item,
                                isSelected = selectedContacts.contains(item),
                                onLongPressClicked = {
                                    if (!longPressActive.value) {
                                        longPressActive.value = true
                                    }

                                    if (selectedContacts.contains(item)) {
                                        selectedContacts.remove(item)
                                    } else {
                                        selectedContacts.add(item)
                                    }
                                    if (selectedContacts.isEmpty()) {
                                        longPressActive.value = false
                                    }
                                    Log.d(
                                        "SelectedContacts",
                                        "Selected Contacts: ${selectedContacts.map { it.contact.name }}"
                                    )
                                },
                                onSingleClick = {
                                    if (longPressActive.value) {
                                        if (selectedContacts.contains(item)) {
                                            selectedContacts.remove(item)
                                        } else {
                                            selectedContacts.add(item)
                                        }
                                    } else {
                                        navHostController.navigate(Destination.PersonalChatRoom(
                                            receiverId = item.contact.userId.toString(),
                                            ""
                                        ))

                                        selectedContacts.clear()
                                    }
                                    if (selectedContacts.isEmpty()) {
                                        longPressActive.value = false
                                    }
                                    Log.d(
                                        "SelectedContacts",
                                        "Selected Contacts: ${selectedContacts.map { it.contact.name }}"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }


    }

    if(isOpenGroupCreateDialog.value) {
        CreateGroupDialog(
            isOpen = isOpenGroupCreateDialog.value,
            onClose = { isOpenGroupCreateDialog.value = false },
            selectedContacts = selectedContacts,
            onCreateGroup = {
                groupName,groupDescription ->
                groupCreateViewModel.createGroup(groupName, groupDescription, selectedContacts)
                isOpenGroupCreateDialog.value = false
                selectedContacts.clear()
                onBackClick()
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactToolBar(
    onBackClick: () -> Boolean,
    selectedContacts: SnapshotStateList<ContactMultipleItem.ContactItem>,
    isOpenGroupCreateDialog: MutableState<Boolean>,
    longPressActive: MutableState<Boolean>
) {
    TopAppBar(
        modifier = Modifier.padding(0.dp),
        navigationIcon = {
            Icon(Icons.Filled.ArrowBack,
                contentDescription = "Back to previous screen",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp)
                    .clip(shape = CircleShape)
                    .clickable {
                        onBackClick()
                    }
            )
        },
        title = {
            Text(text = "Contacts")
        },
        actions = {
            if (selectedContacts.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable {
                        longPressActive.value = true
                        isOpenGroupCreateDialog.value = true
                    }
                ) {
                    Text(
                        text = "Create New Group",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = colorScheme.onSurface
                    )
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Create Group",
                        modifier = Modifier
                            .clip(shape = CircleShape)
                    )

                }

            }

        }
    )
}

