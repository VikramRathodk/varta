package com.devvikram.varta.ui.screens.groupchatroom.groupinfo

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.devvikram.varta.ui.itemmodels.ConversationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupEditDialog(
    onDismiss: () -> Unit,
    groupConversation: State<ConversationItem.GroupConversation?>,
    onGroupEdit: (String, String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        EditGroupInformationLayout(onDismiss, groupConversation, onGroupEdit = onGroupEdit)
    }
}


