package com.devvikram.varta.ui.screens.groupchatroom.groupinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.ui.composable.reusables.MySearchBar
import com.devvikram.varta.ui.screens.groupchatroom.commoncomposable.GroupMembersList
import com.devvikram.varta.ui.screens.groupchatroom.maingroupchatroom.MainGroupChatRoomViewModel

@Composable
fun GroupInfoMemberMultipleScreen(
    navHostController: NavHostController,
    groupInfoViewModel: GroupInfoViewModel,
    mainGroupChatRoomViewModel: MainGroupChatRoomViewModel
) {
    val groupMemberList by mainGroupChatRoomViewModel.groupMembers.collectAsState()
    val searchQuery by groupInfoViewModel.searchQuery.collectAsState()
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            MySearchBar(
                onBackPressed = {
                    navHostController.popBackStack()
                },
                placeholder = "Search members...",
                query =searchQuery,
                onQueryChanged = {
                    groupInfoViewModel.updateSearchQuery(it)
                },
                isBackNavigationEnabled = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            GroupMembersList(
                modifier = Modifier,
                isFixedHeight = false,
                navHostController = navHostController,
                groupInfoViewModel =  groupInfoViewModel,
                mainGroupChatRoomViewModel = mainGroupChatRoomViewModel
            )
        }
    }

}
