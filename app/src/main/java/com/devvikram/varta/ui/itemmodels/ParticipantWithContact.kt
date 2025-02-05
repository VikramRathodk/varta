package com.devvikram.varta.ui.itemmodels

import androidx.room.Embedded
import androidx.room.Relation
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.models.RoomParticipant

class ParticipantWithContact {

    @Embedded
    lateinit var participant: RoomParticipant

    @Relation(parentColumn = "userId", entityColumn = "user_id")
    lateinit var contact: ProContacts

}