package com.devvikram.varta.config.constants

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {

    @Serializable
    data object Home : Destination()

    @Serializable
    data object OnBoarding : Destination()

    @Serializable
    data object Login : Destination()

    @Serializable
    data object Register : Destination()

    @Serializable
    data object GlobalSearch : Destination()

    @Serializable
    data object Conversation : Destination()

    @Serializable
    data object Contact : Destination()

    @Serializable
    data class PersonalChatRoom(
        val receiverId : String,
        val conversationId : String
    ) : Destination()

    @Serializable
    data object PersonalProfileInfo : Destination()


//    TODO Trying new layout
    @Serializable
    data class GroupRoomChat(
    val conversationId: String,
    val conversationName : String,
    val participants : List<String>
    ):Destination()

    @Serializable
    data class GroupChatRoom(
        val conversationId: String,
        val conversationName : String,
        val participants : List<String>
        ): Destination()

    @Serializable
    data class GroupProfileInfo(
        val conversationId : String,
    ) : Destination()

    @Serializable
    data object GroupInfoMemberMultiple : Destination()

    @Serializable
    data object CreateGroup : Destination()

    @Serializable
    data class AddNewMember(
        val conversationId : String,
    ) : Destination()

    @Serializable
    data object GroupMessageInformation : Destination()

    @Serializable
    data object PersonalMessageInformation : Destination()

    @Serializable
    data object SendFile : Destination()
}