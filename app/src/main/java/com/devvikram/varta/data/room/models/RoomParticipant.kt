package com.devvikram.varta.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "participants")
data class RoomParticipant(
    @PrimaryKey(autoGenerate = true)
    val localParticipantId: Long,
    val userId: String,//unknown
    val conversationId: String, // Foreign key reference to conversation
    var role: String? = null // Role of the participant (e.g., "admin", "member")
)
{
    constructor(userId: String, conversationId: String, role: String? = null) :
            this(0, userId, conversationId, role)

    override fun toString(): String {
        return "RoomParticipant(localParticipantId=$localParticipantId, userId='$userId', conversationId='$conversationId', role=$role)"
    }
}
