package com.devvikram.varta.ui.screens.groupchatroom.chatroom.models

sealed class GroupContactItem {
    abstract val proUserId: String
    override fun toString(): String {
        return super.toString()
    }

    data class UnselectedGroupContactItem(
        override val proUserId: String,
        val proNameOfUser: String,
        val statusText: String,
        val isSelected: Boolean,
        val localProfilePath: String
    ) : GroupContactItem()

    data class SelectedGroupContactItem(
        override val proUserId: String,
        val proNameOfUser: String,
        val statusText: String,
        val isSelected: Boolean,
        val localProfilePath: String
    ) : GroupContactItem()

    data class AlreadyAddedGroupContactItem(
        override val proUserId: String,
        val proNameOfUser: String,
        val statusText: String,
        val localProfilePath: String
    ) : GroupContactItem()
}
