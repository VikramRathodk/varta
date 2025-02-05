package com.devvikram.varta.ui.screens.sendFiles

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.MediaType
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.composable.reusables.MessageInput
import com.devvikram.varta.utility.AppUtils
import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.rememberHorizontalPdfReaderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendFileScreen(
    navHostController: NavHostController,
    sendFileViewmodel: SendFileViewmodel
) {

    val selectedFileUri = sendFileViewmodel.selectedFileUri.collectAsState()
    val currentUriIndex = sendFileViewmodel.currentUriIndex.collectAsState()
    val activeConversationName = sendFileViewmodel.conversationName.collectAsState()
    if (selectedFileUri.value != null) {
        println("File Selected : ${selectedFileUri.value}")
    } else {
        println("No File Selected")
    }
    val context = LocalContext.current

    LaunchedEffect(selectedFileUri.value, currentUriIndex.value) {
        println("Current Uri: ${selectedFileUri.value?.get(currentUriIndex.value)}")
    }
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text(activeConversationName.value) },
                navigationIcon = {
                BackNavigationButton {
                    navHostController.popBackStack()
                }
            },
                actions = {
                    TextButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Text(text = "Discard")
                    }
                })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                    val files = selectedFileUri.value
                    if (files != null) {
                        if (files.isNotEmpty()) {
                            val currentUri = files[currentUriIndex.value]
                            val mediaType = AppUtils.getMediaType(
                                context = context,
                                uri = currentUri,
                            )
                            Column (
                                modifier = Modifier
                                   .fillMaxWidth()
                                   .wrapContentHeight(Alignment.CenterVertically)
                                   .clip(RoundedCornerShape(8.dp))
                                   .background(Color.LightGray.copy(
                                       alpha = 0.1f
                                   ))
                                   .padding(16.dp)
                            ){
                                val fileName = AppUtils.getFileName(
                                    currentUri,
                                    context = context
                                )
                                Text(
                                    text = "File Name : $fileName",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                key(currentUri.toString()) {
                                    FilePreviewContainer(
                                        selectedMediaUri = currentUri,
                                        mediaType = mediaType
                                    )
                                }
                            }


                        } else {
                            Text("No files selected")
                        }
                    }else{
                        Text("No files selected")
                    }

            }

            MessageInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onMessageSend = { message ->
                    sendFileViewmodel.sendMessage(message){
                        navHostController.popBackStack()
                    }

                },
                isCameraEnabled = false,
                isAttachmentEnabled = false,
                isReplyMode = false,
                closeReplyView = {},
                replyMessageItem = {},
                onCameraClick = {},
                onAttachmentClick = {},
                hintMessage = "Type a Caption..."
            )

        }


    }
}

@Composable
fun FileTypeContainer(mediaType: MediaType,selectedMediaUri:Uri?) {

    val fileResourceType = if(selectedMediaUri
        == null) {
        when (mediaType) {
            MediaType.EXCEL -> {
                R.drawable.document_file_icon
            }
            MediaType.WORD -> {
                R.drawable.document_file_icon
            }
            else -> {
                R.drawable.document_file_icon
            }
        }
    }else{
        R.drawable.document_file_icon
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = "",
                error = painterResource(fileResourceType)
            ),
            contentDescription = "File Preview",
            modifier = Modifier.fillMaxWidth(0.9f) .clip(
                RoundedCornerShape(16.dp)
            )// 90% of width
        )
    }
}

@Composable
fun ImageContainerSendFileCard(selectedMediaUri: Uri?) {
    val context = LocalContext.current
    val imageSize = AppUtils.findImageSize(selectedMediaUri,context)
    println("imageSize  : $imageSize")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Gray.copy(
                alpha = 0.2f
            )).clip(
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = selectedMediaUri,
                    error = painterResource(R.drawable.image_icon),
                    placeholder = painterResource(R.drawable.image_icon)
                ),
                contentDescription = "File Preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.widthIn(
                    min = 100.dp,
                    max = 200.dp
                ).heightIn(
                    min = 100.dp,
                    max = 200.dp
                ).clip(
                    RoundedCornerShape(16.dp)
                )
            )

    }
}

@Composable
fun PdfContainerSendFileCard(modifier: Modifier, selectedMediaUri: Uri?) {
    val pdfState = rememberHorizontalPdfReaderState(
        resource = ResourceType.Local(selectedMediaUri!!),
        isZoomEnable = true,
    )

    HorizontalPDFReader(
        state = pdfState,
        modifier = modifier
            .background(color = Color.Gray)
    )
}

@Composable
fun FilePreviewContainer(selectedMediaUri: Uri?, mediaType: MediaType) {


    when (mediaType) {
        MediaType.IMAGE -> {
            ImageContainerSendFileCard(selectedMediaUri)
        }
        MediaType.PDF -> {
            PdfContainerSendFileCard(modifier = Modifier.padding(16.dp),selectedMediaUri)
        }
        else -> {
            FileTypeContainer(mediaType= mediaType,selectedMediaUri=selectedMediaUri)
        }
    }

}

