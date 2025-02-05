package com.devvikram.varta.ui.screens.messageinformation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.composable.reusables.DeliveredTimeCard
import com.devvikram.varta.ui.composable.reusables.ReadMessageCard
import com.devvikram.varta.ui.composable.reusables.SentTimeCard

import com.devvikram.varta.ui.screens.personalchatroom.PersonalChatRoomScreen
import com.devvikram.varta.ui.screens.personalchatroom.PersonalChatRoomUtil.Companion.createReceiverMessage
import com.devvikram.varta.ui.screens.personalchatroom.PersonalChatRoomUtil.Companion.createSenderMessage
import com.devvikram.varta.ui.screens.personalchatroom.PersonalChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalMessageInformationScreen(
    navHostController: NavHostController,
    personalChatViewModel: PersonalChatViewModel
) {
    val selectedMessageItem by personalChatViewModel.selectedMessageItem.collectAsState()
    val context = LocalContext.current
    val loginPreference = remember { LoginPreference(context) }
    val contactState = remember { mutableStateOf<Map<String, ProContacts>>(emptyMap()) }





    val filteredMessageList = selectedMessageItem?.let { messageItem ->
        if (messageItem.isMediaMessage) {
            createSenderMessage(RoomMessage(
                conversationId = "",
                messageId ="",
                senderId = "",
                messageType = "",
                timestamp = 0L,
            ))
        } else {
            createReceiverMessage(
                RoomMessage(
                    conversationId = "",
                    messageId ="",
                    senderId = "",
                    messageType = "",
                    timestamp = 0L,
                )
            )
        }
    } ?: emptyList()
    val isDarkMode = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Personal Message Info") }, navigationIcon = {
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


//                Message Item
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 0.dp, max = 350.dp)
                            .background(Color.LightGray.copy(alpha = 0.2f))
                            .padding(vertical = 8.dp)
                    ) {
                        PersonalChatRoomScreen(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            messageWithMediaList = filteredMessageList,
                            onIsSelectionMode = { },
                            onReplyMode = { },
                            onSelectedList = { }
                        )
                    }
                }

//                Read Message Card
                item {
                    ReadMessageCard(
                        filteredMessageList[0],
                    )
                }
//                Deliverd Message Card
                item {
                    DeliveredTimeCard(
                        filteredMessageList[0],
                    )
                }
                //                Send Time Card
                item {
                      SentTimeCard(filteredMessageList[0])
                }

            }

    }

}