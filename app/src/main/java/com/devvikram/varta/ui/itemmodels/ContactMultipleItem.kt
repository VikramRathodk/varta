package com.devvikram.varta.ui.itemmodels

import com.devvikram.varta.data.room.models.ProContacts

sealed class ContactMultipleItem {
    data class ContactItem(val contact: ProContacts) : ContactMultipleItem()
}