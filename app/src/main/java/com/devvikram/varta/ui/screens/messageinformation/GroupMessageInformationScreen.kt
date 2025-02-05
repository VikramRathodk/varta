package com.devvikram.varta.ui.screens.messageinformation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.composable.reusables.UserCardWithTimeDate
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.GroupChatRoomViewmodel


// TODO information : Message list is empty solve later on

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupMessageInformationScreen(
    navHostController: NavHostController,
    groupChatRoomViewmodel: GroupChatRoomViewmodel,
) {
    val selectedMessageItem by groupChatRoomViewmodel.selectedMessageItem.collectAsState()
    val context = LocalContext.current
    val loginPreference = remember { LoginPreference(context) }

    val isDarkMode = isSystemInDarkTheme()
//    val filteredMessageList = selectedMessageItem?.let { messageItem ->
//        if (messageItem.isMediaMessage) {
//            createSenderMessageForGroup()
//        } else {
//            createReceiverMessageForGroup(message)
//        }
//    } ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Message Info") }, navigationIcon = {
                BackNavigationButton(
                    onClick = {
                        navHostController.popBackStack()
                    }
                )
            })
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Message item

//
////                Message Item
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(min = 0.dp, max = 350.dp)
//                            .background(Color.LightGray.copy(alpha = 0.2f))
//                            .padding(vertical = 8.dp)
//                    ) {
//                        GroupChatLazyColumn(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            messageWithMediaList = filteredMessageList,
//                            onIsSelectionMode = { },
//                            onReplyMode = { },
//                            onSelectedList = { }
//                        )
//                    }
//                }
//
////                Read Message Card
//                item {
//                    ReadMessageCard(
//                        filteredMessageList[0],
//                    )
//                }
////                Deliverd Message Card
//                item {
//                    DeliveredTimeCard(
//                        filteredMessageList[0],
//                    )
//                }
//                //                Send Time Card
//                item {
//                    SentTimeCard(filteredMessageList[0])
//                }

            }

    }
}


@Composable
fun <T>UserCardWithTime(selectedMessageItem: T?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(250.dp)
            .nestedScroll(rememberNestedScrollInteropConnection())
    ) {
        items(20) {
            UserCardWithTimeDate(selectedMessageItem)
        }
    }

}



