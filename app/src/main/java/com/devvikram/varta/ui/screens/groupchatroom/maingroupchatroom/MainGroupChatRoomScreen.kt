package com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.GroupChatChatRoomScreen
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.GroupChatRoomViewmodel
import com.devvikram.varta.ui.screens.groupchatroom.addnewmembers.AddNewMemberScreen
import com.devvikram.varta.ui.screens.groupchatroom.addnewmembers.GroupAddNewViewModel
import com.devvikram.varta.ui.screens.groupchatroom.groupinfo.GroupInfoMemberMultipleScreen
import com.devvikram.varta.ui.screens.groupchatroom.groupinfo.GroupInfoViewModel
import com.devvikram.varta.ui.screens.groupchatroom.groupinfo.GroupProfileInfoScreen
import com.devvikram.varta.ui.screens.sendFiles.SendFileViewmodel


// Screen will be GroupChatRoomScreen , group profile information, view members, add new members, create group

@Composable
fun MainGroupChatRoomScreen(
    mainGroupChatRoomViewModel: MainGroupChatRoomViewModel,
    mainNavigationController: NavHostController,
    groupChatRoom: Destination.GroupRoomChat
) {
    val groupNavController = rememberNavController()
    val sendFileViewmodel = SendFileViewmodel()
    val groupInfoViewmodel  : GroupInfoViewModel = hiltViewModel<GroupInfoViewModel>()

    LaunchedEffect (
        groupChatRoom.conversationId
    ){
        mainGroupChatRoomViewModel.setCurrentConversationId(groupChatRoom.conversationId)
    }

    NavHost(
        navController = groupNavController,
        startDestination = Destination.GroupChatRoom(
            conversationId = groupChatRoom.conversationId,
            conversationName = groupChatRoom.conversationName,
            participants = groupChatRoom.participants
        )
    ){
//        Group ChatScreen means messages and all
            composable<Destination.GroupChatRoom> {
                val currentConversation  =  it.toRoute<Destination.GroupChatRoom>()
                val groupChatRoomViewmodel : GroupChatRoomViewmodel = hiltViewModel()

                GroupChatChatRoomScreen(
                    mainNavigationController = mainNavigationController,
                    groupChatNavHostController = groupNavController,
                    groupChatRoomViewmodel = groupChatRoomViewmodel,
                    mainGroupChatRoomViewModel = mainGroupChatRoomViewModel,
                    sendFileViewmodel = sendFileViewmodel,
                    currentConveration =currentConversation
                )
        }
//        Group profile screen
        composable<Destination.GroupProfileInfo> {
            val groupProfile = it.toRoute<Destination.GroupProfileInfo>()

            GroupProfileInfoScreen(
                navHostController = groupNavController,
                groupInfoViewModel = groupInfoViewmodel,
                mainGroupChatRoomViewModel = mainGroupChatRoomViewModel,
                currentConversationId  = groupProfile.conversationId,
            )
        }

        composable<Destination.GroupInfoMemberMultiple>(
        ) {
            GroupInfoMemberMultipleScreen(
                navHostController = groupNavController,
                groupInfoViewModel = groupInfoViewmodel,
                mainGroupChatRoomViewModel = mainGroupChatRoomViewModel,
            )
        }

        composable<Destination.AddNewMember> {
            val currentConversation  =  it.toRoute<Destination.AddNewMember>()
            val groupAddNewViewModel : GroupAddNewViewModel = hiltViewModel()
            AddNewMemberScreen(
                navHostController = groupNavController,
                viewModel = groupAddNewViewModel,
                conversationId = currentConversation.conversationId
            )
        }

    }

}