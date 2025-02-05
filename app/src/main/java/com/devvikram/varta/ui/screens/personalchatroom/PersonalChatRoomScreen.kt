package com.devvikram.varta.ui.screens.personalchatroom

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem
import com.devvikram.varta.ui.screens.personalchatroom.headers.SelectionModeTopBar
import com.devvikram.varta.ui.screens.personalchatroom.headers.UserProfileHeader
import com.devvikram.varta.ui.screens.sendFiles.SendFileViewmodel
import com.devvikram.varta.utility.AppUtils


@Composable
fun PersonalChatRoomScreen(
    navHostController: NavHostController,
    personalChatViewModel: PersonalChatViewModel,
    sendFileViewmodel: SendFileViewmodel,
    receiverId : String,
    currentConversationid : String
) {

    println("PersonalChatRoomScreen: ReceiverId: $receiverId")

    LaunchedEffect(true) {
        println("PersonalChatRoomScreen currentConversationid: $currentConversationid")
        personalChatViewModel.updateConversationId(currentConversationid)
        personalChatViewModel.getReceiverInfo(receiverId)
    }
    val receiverProfile = personalChatViewModel.receiverUserProfile.collectAsState().value

    var isAttachmentDialogOpen by remember { mutableStateOf(false) }

    var selectedMessageList by remember { mutableStateOf<Set<PersonalChatMessageItem>>(emptySet()) }
    var isSelectionMode by remember { mutableStateOf(false) }
    var isReplyMode by remember { mutableStateOf(false) }
    val userProfile by personalChatViewModel.userProfile.collectAsState()
    val messagesList by personalChatViewModel.messagesList.collectAsState()


    val context = LocalContext.current;


    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uri ->
            uri.let {
                userProfile?.let { it1 -> sendFileViewmodel.updateConversationName(it1.name) }
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri = uri.toMutableList())
                navHostController.navigate(Destination.SendFile)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                val uri = AppUtils.saveBitmapToFile(context, it)
                userProfile?.let { it1 -> sendFileViewmodel.updateConversationName(it1.name) }
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri = mutableListOf(uri))
                navHostController.navigate(Destination.SendFile)

            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris->
            uris.let {
                userProfile?.let { it1 -> sendFileViewmodel.updateConversationName(it1.name) }
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri = uris.toMutableList())
                navHostController.navigate(Destination.SendFile)
            }
        }
    )

    val audioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uri ->
            uri.let {
                userProfile?.let { it1 -> sendFileViewmodel.updateConversationName(it1.name) }
                sendFileViewmodel.sendSelectedFileUri(selectedFileUri =
                uri.toMutableList())
                navHostController.navigate(Destination.SendFile)
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
                    userProfile?.let { it1 -> sendFileViewmodel.updateConversationName(it1.name) }
                    sendFileViewmodel.sendSelectedFileUri(selectedFileUri = mutableListOf(locationUri))
                    navHostController.navigate(Destination.SendFile)
                }
            }
        }
    )

    val isDarkMode = isSystemInDarkTheme()
    val backGroupResource = if(isDarkMode){
        R.drawable.chat_background_dark
    }else{
        R.drawable.prochat_chat_bg_light
    }


    BackHandler {
        if (isSelectionMode) {
            isSelectionMode = false
            selectedMessageList = emptySet()
        } else {
            navHostController.popBackStack()
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
                    onForward = {
                        personalChatViewModel.forwardMessage(selectedMessageList.toList())
                        isSelectionMode = false
                        selectedMessageList = emptySet()
                    },
                    onReply = {
                        isReplyMode = true
                    },
                    onCopy = {
                        AppUtils.copyChatMessageContent(
                            context = context,
                            selectedMessageList.toList()
                        )
                        selectedMessageList = emptySet()
                        isSelectionMode = false
                    },
                    onInfo = {
                        personalChatViewModel.updateSelectedMessageItem(selectedMessageList.firstOrNull())
                        navHostController.navigate(Destination.PersonalMessageInformation)
                    },
                    onUnselectionAll = {
                        selectedMessageList = emptySet()
                        isSelectionMode = false
                    },
                    onDeactivateReplyMode = {
                        isReplyMode = false
                    }
                )
            } else {
                isReplyMode = false
                UserProfileHeader(
                    personalChatViewModel = personalChatViewModel,
                    currentReceiver = receiverProfile,
                    onBackPressed = { navHostController.popBackStack() },
                    onClick = {
                        navHostController.navigate(Destination.PersonalProfileInfo) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = backGroupResource),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
                .padding(it)
        ) {

            PersonalChatRoomScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                messageWithMediaList = messagesList.toList(),
                onIsSelectionMode = { isSelectionMode = it },
                onReplyMode = { isReplyMode = it },
                onSelectedList = {
                    newSelectedList ->
                    selectedMessageList = newSelectedList.toSet()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))


            if (isAttachmentDialogOpen) {
                AttachmentDialog(onDismiss = { isAttachmentDialogOpen = false }, onOptionSelected = {
                    selectedOption ->
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
            MessageInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onMessageSend = { message ->
                    personalChatViewModel.sendMessage(message)
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





