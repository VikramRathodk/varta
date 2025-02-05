package com.devvikram.varta.ui.screens.conversations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.itemmodels.ConversationItem

@Composable
fun ConversationScreen(
    homeNavigationController: NavHostController,
    conversationViewmodel: ConversationViewmodel,
    mainNavController: NavHostController,
) {
    val conversations by conversationViewmodel.conversations.collectAsState(initial = emptyList())
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background,),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//            filter options
            FilterBar(
                listOfFilterOptions = conversationViewmodel.listOfFilterOptions,
            )
//            conversations
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(conversations.size) { index->
                    println("Conversations with items size  $index")
                    val conversationWithContact = conversations[index]
                    val conversation = conversationWithContact.conversation
                    val contact = conversationWithContact.contact

                            val conversationItem = when (conversation.type) {
                                "P" -> ConversationItem.PersonalConversation(
                                    conversationId = conversation.conversationId,
                                    proContactName = contact?.name.toString(),
                                    lastMessage = "",
                                    lastMessageSender = 1,
                                    lastMessageStatus = "sent",
                                    unreadMessageCount = 0,
                                    gender = 1,
                                    )

                                else -> {
                                    ConversationItem.GroupConversation(
                                        conversationId = conversation.conversationId,
                                        groupName = conversation.name.toString(),
                                        lastMessage = "",
                                        lastMessageSender = 1,
                                        lastMessageStatus = "sent",
                                        unreadMessageCount = 0,
                                        gender = 1,
                                        participants = conversation.participantIds
                                    )
                                }
                            }
                            when (conversationItem) {
                                is ConversationItem.PersonalConversation -> {
                                    PersonalConversationItemView(
                                        conversationItem,
                                        onClick = {
                                            mainNavController.navigate(Destination.PersonalChatRoom(
                                                conversationId = conversationItem.conversationId,
                                                receiverId =  conversation.userId.toString()
                                            ))
                                        }
                                    )
                                }

                                is ConversationItem.GroupConversation -> {
                                    GroupConversationItemView(
                                        conversationItem,
                                    ) {
                                        mainNavController.navigate(Destination.GroupRoomChat(
                                            conversationId = conversationItem.conversationId,
                                            conversationName = conversationItem.groupName,
                                            participants = conversationItem.participants
                                        )
                                        )
                                    }
                                }
                            }
                }
            }
        }

    }


}







