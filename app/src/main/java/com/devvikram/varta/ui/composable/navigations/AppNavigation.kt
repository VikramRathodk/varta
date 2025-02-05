package com.devvikram.varta.ui.composable.navigations

import PersonalProfileInfoScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.screens.creategroup.CreateNewGroupScreen
import com.devvikram.varta.ui.screens.globalsearch.GlobalSearchScreen
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.GroupChatRoomViewmodel
import com.devvikram.varta.ui.screens.messageinformation.GroupMessageInformationScreen
import com.devvikram.varta.ui.screens.messageinformation.PersonalMessageInformationScreen
import com.devvikram.varta.ui.screens.personalchatroom.PersonalChatRoomScreen
import com.devvikram.varta.ui.screens.personalchatroom.PersonalChatViewModel
import com.devvikram.varta.ui.screens.sendFiles.SendFileScreen
import com.devvikram.varta.ui.screens.sendFiles.SendFileViewmodel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.toRoute
import com.devvikram.varta.AppViewModel
import com.devvikram.varta.ui.screens.home.HomeScreen
import com.devvikram.varta.ui.screens.creategroup.CreateGroupViewModel
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomScreen
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    appViewModel: AppViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Destination.Home
    ) {
        val sendFileViewmodel = SendFileViewmodel()


        /*
                ----------------------- Home Screen -----------------------
        */

        composable<Destination.Home> {
            HomeScreen(appViewModel = appViewModel,mainNavController = navController)
        }

//          ----------------------------- Chat Rooms --------------------------------

        composable<Destination.PersonalChatRoom> {
            val personalChatViewModel : PersonalChatViewModel = hiltViewModel()
            val personalChatRoom = it.toRoute<Destination.PersonalChatRoom>()

            println("PersonalChatRoom: $personalChatRoom")
            PersonalChatRoomScreen(
                navHostController = navController,
                personalChatViewModel = personalChatViewModel,
                sendFileViewmodel = sendFileViewmodel,
                receiverId = personalChatRoom.receiverId,
                currentConversationid = personalChatRoom.conversationId
            )
        }

        composable<Destination.GroupRoomChat> {
            val mainGroupChatRoomViewModel : MainGroupChatRoomViewModel = hiltViewModel()
            val groupChatRoom = it.toRoute<Destination.GroupRoomChat>()
            MainGroupChatRoomScreen(mainGroupChatRoomViewModel,navController,groupChatRoom)
        }

        composable<Destination.PersonalProfileInfo> {
            PersonalProfileInfoScreen(
                navHostController = navController,
            )
        }


        composable<Destination.CreateGroup> {
            val groupCreateViewModel : CreateGroupViewModel = hiltViewModel<CreateGroupViewModel>()
            CreateNewGroupScreen(
                navHostController = navController,
                viewModel = groupCreateViewModel
            )
        }


        composable<Destination.GroupMessageInformation> {
            val groupChatRoomViewmodel : GroupChatRoomViewmodel = hiltViewModel<GroupChatRoomViewmodel>()
            GroupMessageInformationScreen(
                navHostController = navController,
                groupChatRoomViewmodel = groupChatRoomViewmodel,
            )
        }

        composable<Destination.PersonalMessageInformation> {
            val personalChatViewModel : PersonalChatViewModel = hiltViewModel<PersonalChatViewModel>()
            PersonalMessageInformationScreen(
                navHostController = navController,
                personalChatViewModel = personalChatViewModel,
            )
        }

        /*
                ----------------------- Global Search  -----------------------
        */
        composable<Destination.GlobalSearch> {
            GlobalSearchScreen(
                navHostController = navController
            )
        }


        composable<Destination.SendFile> {navBackStackEntry->
            SendFileScreen(
                navHostController = navController,
                 sendFileViewmodel = sendFileViewmodel
            )
        }
    }
}




