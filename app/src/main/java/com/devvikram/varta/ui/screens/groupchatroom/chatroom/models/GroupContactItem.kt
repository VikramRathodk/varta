package com.devvikram.varta.ui.screens.groupchatroom.chatroom.models

sealed class GroupContactItem {
    abstract val proUserId: Int
    override fun toString(): String {
        return super.toString()
    }

    data class UnselectedGroupContactItem(
        override val proUserId: Int,
        val proNameOfUser: String,
        val proDesignation: String,
        val isSelected: Boolean,
        val localProfilePath: String
    ) : GroupContactItem()

    data class SelectedGroupContactItem(
        override val proUserId: Int,
        val proNameOfUser: String,
        val proDesignation: String,
        val isSelected: Boolean,
        val localProfilePath: String
    ) : GroupContactItem()

    data class AlreadyAddedGroupContactItem(
        override val proUserId: Int,
        val proNameOfUser: String,
        val proDesignation: String,
        val localProfilePath: String
    ) : GroupContactItem()
}
