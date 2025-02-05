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
        val userId: Int,
        val name: String,
        val profileImage: String,
        val designation: String,
    ) : GroupMemberItem()

    @Serializable
    data class GroupCreater(val userId: Int, val name: String, val profileImage: String, val designation: String,
    ) : GroupMemberItem()

    @Serializable
    data class GroupAdmin(val userId: Int, val name: String, val profileImage: String,val designation: String,
    ) : GroupMemberItem()
}