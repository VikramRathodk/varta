package com.devvikram.varta.utility

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.screens.sendFiles.SendFileViewmodel


class ActivityResultLaunchers {

    companion object {
        @Composable
        fun <T> DocumentLauncher(
            data: T,
            sendFileViewmodel: SendFileViewmodel,
            navHostController: NavHostController,
            onUpdate: (T, List<Uri>) -> Unit,
            onDocumentLaunch: (documentLauncher: ActivityResultLauncher<Array<String>>) -> Unit
        ) {
            val documentLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenMultipleDocuments(),
                onResult = { uris ->
                    uris.let {
                        onUpdate(data, uris)
                        sendFileViewmodel.sendSelectedFileUri(selectedFileUri = uris.toMutableList())
                        navHostController.navigate(Destination.SendFile)
                    }
                }
            )
            onDocumentLaunch(documentLauncher)
        }
    }

}