package com.devvikram.varta.ui.screens.groupchatroom.groupinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.devvikram.varta.R
import com.devvikram.varta.ui.itemmodels.ConversationItem
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupIconModel

@Composable
fun EditGroupInformationLayout(
    onClose: () -> Unit,
    group: State<ConversationItem.GroupConversation?>,
    onGroupEdit: (String, String) -> Unit
) {

    val groupName = remember { group.value?.let { mutableStateOf(it.groupName) } }
    val groupDescription = remember { group.value?.let { mutableStateOf(it.groupDescription) } }
    val errorMessage = remember { mutableStateOf("") }

    // Group icons for selections
    val groupIcons = listOf<GroupIconModel>(
        GroupIconModel("Group Icon 1", R.drawable.group_icon),
        GroupIconModel("Group Icon 2", R.drawable.baseline_person_24),
        GroupIconModel("Group Icon 3", R.drawable.group_icon),
        GroupIconModel("Group Icon 4", R.drawable.varta_logo),
        GroupIconModel("Group Icon 5", R.drawable.baseline_star_24_filled),
        GroupIconModel("Group Icon 6", R.drawable.baseline_visibility_24),
        GroupIconModel("Group Icon 7", R.drawable.baseline_photo_camera_24),
        GroupIconModel("Group Icon 8", R.drawable.image_icon),
        GroupIconModel("Group Icon 9", R.drawable.document_file_icon),
        GroupIconModel("Group Icon 10", R.drawable.gallary_icon)
    )
    val selectedGroupIcon = remember {
        mutableStateOf(groupIcons.first())
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Group Icon
        Image(
            painter = rememberAsyncImagePainter(
                model = selectedGroupIcon.value.iconResId,
                error = painterResource(id = R.drawable.group_icon)
            ),
            contentDescription = "Group Icon",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        // Error message handle here
        if (errorMessage.value != "") {
            Text(
                text = errorMessage.value,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        } else {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(16.dp))
        }

        // Group Name Input
        groupName?.let {
            OutlinedTextField(
                value = it.value,
                onValueChange = {
                    groupName.value = it
                },
                isError = errorMessage.value == "Group Name is required",
                label = { Text("Group Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Group Description Input
        if (groupDescription != null) {
            OutlinedTextField(
                value = groupDescription.value,
                onValueChange = {
                    groupDescription.value = it
                },
                isError = errorMessage.value == "Group Description is required",
                label = { Text("Group Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                ),
                maxLines = 5
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            items(groupIcons.size) { index ->
                Image(
                    painter = painterResource(
                        id = groupIcons[index].iconResId
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable {
                            selectedGroupIcon.value = groupIcons[index]
                        }
                )
                if (index < groupIcons.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onClose() },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (groupName?.value?.isEmpty() == true) {
                        errorMessage.value = "Group Name is required"
                        return@Button
                    }
                    if (groupDescription != null) {
                        if (groupDescription.value.isEmpty()) {
                            errorMessage.value = "Group Description is required"
                            return@Button
                        }
                    }
                    onClose()
                    if (groupName != null) {
                        if (groupDescription != null) {
                            onGroupEdit(groupName.value, groupDescription.value)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Save Changes")
            }
        }
    }
}