package com.devvikram.varta.ui.screens.groupchatroom.chatroom.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
sealed class GroupMemberItem : Parcelable {
    @Serializable
    data class GroupAddNewMember(val title: String) : GroupMemberItem()

    @Serializable
    data class GroupMember(
        val userId: String,
        val name: String,
        val profileImage: String,
        val statusText: String,
    ) : GroupMemberItem()

    @Serializable
    data class GroupCreater(val userId: String, val name: String, val profileImage: String, val statusText: String,
    ) : GroupMemberItem()

    @Serializable
    data class GroupAdmin(val userId: String, val name: String, val profileImage: String,val statusText: String,
    ) : GroupMemberItem()
}