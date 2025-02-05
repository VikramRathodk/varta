package com.devvikram.varta.ui.screens.groupchatroom.chatroom

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.composable.reusables.AttachmentDialog
import com.devvikram.varta.ui.composable.reusables.MessageInput
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomViewModel
import com.devvikram.varta.ui.screens.sendFiles.SendFileViewmodel
import com.devvikram.varta.utility.AppUtils

@Composable
fun GroupChatChatRoomScreen(
    groupChatNavHostController: NavHostController,
    groupChatRoomViewmodel: GroupChatRoomViewmodel,
    sendFileViewmodel: SendFileViewmodel,
    currentConveration: Destination.GroupChatRoom,
    mainNavigationController: NavHostController,
    mainGroupChatRoomViewModel: MainGroupChatRoomViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(true) {
        groupChatRoomViewmodel.updateConversationId (currentConveration.conversationId)
    }

    var isAttachmentDialogOpen by remember { mutableStateOf(false) }

    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedMessageList by remember { mutableStateOf<Set<GroupChatMessageItem>>(emptySet()) }
    var isReplyMode by remember { mutableStateOf(false) }
    val messagesList by groupChatRoomViewmodel.messagesList.collectAsState()


    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uri ->
            uri.let {
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri = uri.toMutableList())
                groupChatNavHostController.navigate(Destination.SendFile)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                val uri = AppUtils.saveBitmapToFile(context, it)
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri = mutableListOf(uri))
                groupChatNavHostController.navigate(Destination.SendFile)

            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris->
            uris.let {
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri = uris.toMutableList())
                groupChatNavHostController.navigate(Destination.SendFile)
            }
        }
    )

    val audioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uri ->
            uri.let {
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri =
                uri.toMutableList())
                groupChatNavHostController.navigate(Destination.SendFile)
            }
        }
    )

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val locationUri = data?.data
                locationUri?.let {
                    sendFileViewmodel.sendSelectedFileUri(selectedFileUri = mutableListOf(locationUri))
                    groupChatNavHostController.navigate(Destination.SendFile)
                }
            }
        }
    )

    val isDarkMode = isSystemInDarkTheme()
    val backGroupResource = if(isDarkMode){
        R.drawable.prochat_chat_bg_dark
    }else{
        R.drawable.prochat_chat_bg_light
    }


    BackHandler {
        if (isSelectionMode) {
            isSelectionMode = false
            selectedMessageList = emptySet()
        } else {
            groupChatNavHostController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            if (isSelectionMode) {
                SelectionModeTopBar(
                    selectedCount = selectedMessageList.size,
                    onCloseSelection = {
                        isSelectionMode = false
                        selectedMessageList = emptySet()
                    },
                    onInfo = {
                        if (selectedMessageList.isNotEmpty()) {
                            println("selectedMessageList is : " + selectedMessageList.first())
                            groupChatRoomViewmodel.setSelectedChatMessageItem(selectedMessageList.first())
                            groupChatNavHostController.navigate(Destination.GroupMessageInformation)
                        } else {
                            println("No messages selected.")
                        }
                    },
                    onCopy = {
                        AppUtils.copyChatMessageContent(
                            context, selectedChatMessageList = selectedMessageList.toList()
                        )
                    },
                    onForward = {
                        groupChatRoomViewmodel.forwardMessage(selectedMessageList.toList())
                    },
                    onReply = {
                        isReplyMode = true },
                    onUnselectionAll = {
                        selectedMessageList = emptySet()
                    },
                    onDeactivateReplyMode = {
                        isReplyMode = false
                    }
                )
            } else {
                GroupChatTopAppBar(
                    modifier = Modifier,
                    navHostController = groupChatNavHostController,
                    groupChatRoomViewmodel = groupChatRoomViewmodel,
                    onNavigationClick = {
                        mainNavigationController.popBackStack()
                    },
                    mainGroupChatRoomViewModel = mainGroupChatRoomViewModel

                )
            }

        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .paint(
                    painterResource(id = backGroupResource),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
                .verticalScroll(rememberScrollState())
        ) {

            GroupChatLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                messageWithMediaList = messagesList.toList(),
                onIsSelectionMode = { isSelectionMode = it },
                onReplyMode = { isReplyMode = it },
                onSelectedList = { newSelectedList ->
                    println("Selected Message list is ${newSelectedList}")
                    selectedMessageList = newSelectedList.toSet()

                }
            )

            // open a attachment dialog just about the message input box
            if (isAttachmentDialogOpen) {
                AttachmentDialog(
                    onDismiss = { isAttachmentDialogOpen = false },
                    onOptionSelected = { selectedOption ->
                        when (selectedOption) {
                            "Document" -> {
                                documentLauncher.launch(
                                    arrayOf(
                                        "application/pdf",
                                        "application/msword",
                                        "application/vnd.ms-excel"
                                    )
                                )
                            }
                            "Camera" -> {
                                cameraLauncher.launch(null)
                            }

                            "Gallery" -> {
                                galleryLauncher.launch("image/*")
                            }

                            "Audio" -> {
                                audioLauncher.launch("audio/*")

                            }
                            "Location" -> {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="))
                                locationLauncher.launch(intent)
                            }
                        }
                    })
            }

            // Message Input
            MessageInput(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onMessageSend = { message ->
                    groupChatRoomViewmodel.sendMessage(message)
                },
                isReplyMode = isReplyMode,
                closeReplyView = {
                    isReplyMode = false
                    selectedMessageList = emptySet()
                    isSelectionMode = false
                },
                replyMessageItem = selectedMessageList.firstOrNull(),
                onCameraClick = {},
                onAttachmentClick = {
                    isAttachmentDialogOpen = true
                }
            )
        }
    }
}










