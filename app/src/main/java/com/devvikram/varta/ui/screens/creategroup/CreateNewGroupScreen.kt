package com.devvikram.varta.ui.screens.creategroup

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.itemmodels.ContactMultipleItem
import com.devvikram.varta.ui.screens.contacts.SingleContactCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewGroupScreen(
    navHostController: NavHostController,
    viewModel: CreateGroupViewModel
) {
    val contacts by viewModel.contacts.collectAsState(emptyList())
    val items = remember { mutableStateListOf<ContactMultipleItem>() }
    LaunchedEffect(contacts.size.hashCode()) {
        val contactItems = contacts.map { ContactMultipleItem.ContactItem(it) }
        items.clear()
        items.addAll(contactItems)
    }

    val selectedContacts = remember { mutableStateListOf<ContactMultipleItem.ContactItem>() }
    val isOpenGroupCreateDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = "Create a new Group")
            }, navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            })
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SelectedContactsRow(selectedContacts)

            //  contact list here
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(items.size) { index ->
                    val item = items[index]
                    if (item  is ContactMultipleItem.ContactItem) {
                            SingleContactCard(contactMultipleItem = item,
                                isSelected = selectedContacts.contains(item),
                                onLongPressClicked = {

                                    if (selectedContacts.contains(item)) {
                                        selectedContacts.remove(item)
                                    } else {
                                        selectedContacts.add(item)
                                    }

                                    Log.d(
                                        "SelectedContacts",
                                        "Selected Contacts: ${selectedContacts.map { it.contact.name }}"
                                    )
                                },
                                onSingleClick = {
                                    if (selectedContacts.contains(item)) {
                                        selectedContacts.remove(item)
                                    } else {
                                        selectedContacts.add(item)
                                    }
                                    Log.d(
                                        "SelectedContacts",
                                        "Selected Contacts: ${selectedContacts.map { it.contact.name }}"
                                    )
                                })
                    }
                }
            }
            if (selectedContacts.isNotEmpty()) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                    onClick = {
                        isOpenGroupCreateDialog.value = true
                    }
                ) {
                    Text(
                        text = "Add to group",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }
        }

    }
    // open group create dialog to tak reinforce such as name,icon,and description
    CreateGroupDialog(
        isOpen = isOpenGroupCreateDialog.value,
        onClose = { isOpenGroupCreateDialog.value = false },
        selectedContacts = selectedContacts,
        onCreateGroup = { groupName,groupDescription ->
            if(selectedContacts.isNotEmpty()){
                viewModel.createGroup(groupName,groupDescription,selectedContacts)
            }else{
                Log.d("SelectedContacts", "No contacts selected")
            }
            isOpenGroupCreateDialog.value = false
            selectedContacts.clear()
            navigateToConversation(navHostController)
        }
    )
}

fun navigateToConversation(navHostController: NavHostController) {
    navHostController.navigate(
        Destination.Conversation
    ) {
        popUpTo(Destination.Conversation) {
            inclusive = true
        }
    }
}
@Composable
fun SelectedContactsRow(selectedContacts: MutableList<ContactMultipleItem.ContactItem>) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedContacts.size) {
        if (selectedContacts.isNotEmpty()) {
            listState.scrollToItem(selectedContacts.size - 1)
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        items(selectedContacts) { contactItem ->
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            contactItem.contact.localProfilePicPath, error =
                            painterResource(id = R.drawable.baseline_person_24)
                        ),
                        contentDescription = "Contact Avatar",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                    )

                    IconButton(
                        onClick = {
                            selectedContacts.remove(contactItem)
                            Log.d(
                                "SelectedContacts",
                                "Selected Contacts: ${selectedContacts.map { it.contact.name }}"
                            )
                        },
                        modifier = Modifier
                            .offset(4.dp, 4.dp)
                            .size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove contact",
                        )
                    }
                }

                // Name Below the Profile
                Text(
                    text = if (contactItem.contact.name.length > 8) {
                        "${contactItem.contact.name.take(8)}..."
                    } else {
                        contactItem.contact.name
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .padding(top = 70.dp) // Position below the image
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
