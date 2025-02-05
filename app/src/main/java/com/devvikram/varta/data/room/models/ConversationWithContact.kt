package com.devvikram.varta.data.room.models

import androidx.room.Embedded
import androidx.room.Relation


// ---------------------------------------------------------------- Conversation to contact relationship ----------------------------------------------------------------

data class ConversationWithContact(
    @Embedded
    val conversation: RoomConversation,

    @Relation(
        parentColumn = "userId",
        entityColumn = "user_id"
    )
    val contact: ProContacts? = null
)
