

package com.devvikram.varta.ui.screens.groupchatroom.groupinfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.composable.reusables.buttons.CommonButtons
import com.devvikram.varta.ui.itemmodels.ConversationItem
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupMemberItem
import com.devvikram.varta.ui.screens.groupchatroom.commoncomposable.GroupMembersList
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupProfileInfoScreen(
    navHostController: NavHostController,
    groupInfoViewModel: GroupInfoViewModel,
    currentConversationId: String,
    mainGroupChatRoomViewModel: MainGroupChatRoomViewModel,
) {
    val groupMemberList = groupInfoViewModel.groupMembers.collectAsState().value
    val groupInformation = mainGroupChatRoomViewModel.groupInformation.collectAsState()
    val isEditDialogOpen by groupInfoViewModel.isEditDialogOpen.collectAsState()

    LaunchedEffect(currentConversationId.hashCode()) {
        groupInfoViewModel.setCurrentConversationId(currentConversationId)
        groupInfoViewModel.updateGroupMembers(mainGroupChatRoomViewModel.groupMembers.value)
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {}, actions = {
            IconButton(
                onClick = {
                    groupInfoViewModel.updateEditDialogVisibility(true)
                }, modifier = Modifier.clip(
                    shape = CircleShape
                )
            ) {
                Icon(Icons.Filled.Edit, contentDescription = null)
            }
        }, navigationIcon = {
            BackNavigationButton{
                navHostController.popBackStack()
            }
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(it)
        ) {
            GroupHeaderCard(modifier = Modifier,groupInformation)

            GroupMemberListCard(
                modifier = Modifier,
                groupMemberList = groupMemberList,
                onSearchClick = {
                    navHostController.navigate(Destination.GroupInfoMemberMultiple)
                },
                viewAllMembers = { navHostController.navigate(Destination.GroupInfoMemberMultiple) },
                navHostController = navHostController,
                groupInfoViewModel = groupInfoViewModel,
                mainGroupChatRoomViewMode= mainGroupChatRoomViewModel
            )

            CommonButtons.ExitButton(
                text = "Exit Group",
                isEnabled = true,
                onClick = {
                    // TODO Exits Group itself
                }
            )

        }

    }

    if (isEditDialogOpen) {
        GroupEditDialog(onDismiss = {
            groupInfoViewModel.updateEditDialogVisibility(false)
        }, onGroupEdit = { groupName, groupDescription ->
            mainGroupChatRoomViewModel.updateGroupInfo(
                conversationId = currentConversationId,
                groupName,
                groupDescription
            )
            groupInfoViewModel.updateEditDialogVisibility(false)
        },
            groupConversation = groupInformation
        )
    }
}


@Composable
fun GroupMemberListCard(
    modifier: Modifier,
    groupMemberList: ArrayList<GroupMemberItem>,
    onSearchClick: () -> Unit = {},
    viewAllMembers: () -> Unit = {},
    navHostController: NavHostController,
    groupInfoViewModel: GroupInfoViewModel,
    mainGroupChatRoomViewMode: MainGroupChatRoomViewModel,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
        ),
        border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
//            group Member and Search bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                no of group members
                Text(
                    text = groupMemberList.size.toString() + " members",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Member",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable {
                            onSearchClick()
                        }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            GroupMembersList(
                modifier = Modifier,
                isFixedHeight = true,
                navHostController = navHostController,
                groupInfoViewModel = groupInfoViewModel,
                mainGroupChatRoomViewModel = mainGroupChatRoomViewMode
            )

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "View All Members",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier
                        .align(
                            Alignment.CenterEnd
                        )
                        .clickable {
                            viewAllMembers()
                        }
                )
            }
        }
    }
}

@Composable
fun GroupHeaderCard(
    modifier: Modifier = Modifier,
    groupInformation: State<ConversationItem.GroupConversation?>
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
        ),
        border = BorderStroke(
            width = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Group Image
            Image(
                painter = painterResource(R.drawable.varta_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Group Name
            groupInformation.value?.let {
                Text(
                    text = it.groupName, style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Group Description
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 50.dp, max = 150.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                groupInformation.value?.let {
                    Text(
                        text = it.groupDescription, style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ), textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



