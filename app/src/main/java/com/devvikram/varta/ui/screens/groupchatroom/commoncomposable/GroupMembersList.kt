package com.devvikram.varta.ui.screens.groupchatroom.commoncomposable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.composable.reusables.headerrow.HeaderRow
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupMemberItem
import com.devvikram.varta.ui.screens.groupchatroom.groupinfo.GroupInfoViewModel
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomViewModel

@Composable
fun GroupMembersList(
    modifier: Modifier,
    isFixedHeight: Boolean,
    navHostController: NavHostController,
    groupInfoViewModel: GroupInfoViewModel,
    mainGroupChatRoomViewModel: MainGroupChatRoomViewModel
) {
    val groupMemberList by mainGroupChatRoomViewModel.groupMembers.collectAsState()
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isFixedHeight) 200.dp else Dp.Unspecified)
            .padding(horizontal = 8.dp)
    ) {
        item {
            HeaderRow(
                title = "Add New Member",
                icon = R.drawable.baseline_group_add_24,
                iconSize = 32.dp,
                onClickListener = {
                    navHostController.navigate(Destination.AddNewMember(
                        conversationId = groupInfoViewModel.currentConversationId.value
                    ))
                }
            )
        }

        itemsIndexed(groupMemberList) { index, item ->
                when (item) {
                    is GroupMemberItem.GroupCreater ->{
                        GroupCreatorRow(
                            name = item.name,
                            designation = item.designation,
                            profileImage = item.profileImage,
                            mainGroupChatRoomViewModel = mainGroupChatRoomViewModel,
                            userId = item.userId.toString()
                        )
                    }

                    is GroupMemberItem.GroupMember -> {
                        GroupMemberRow(
                            name = item.name,
                            designation = item.designation,
                            profileImage = item.profileImage,
                            mainGroupChatRoomViewModel = mainGroupChatRoomViewModel,
                            userId = item.userId.toString()
                        )
                    }
                    is GroupMemberItem.GroupAdmin -> {
                        GroupAdminRow(
                            name = item.name,
                            profileImage = item.profileImage,
                            mainGroupChatRoomViewModel = mainGroupChatRoomViewModel,
                            designation = item.designation,
                            userId = item.userId.toString()
                        )
                    }

                    is GroupMemberItem.GroupAddNewMember ->{}
                }
            }

    }

}


