package com.devvikram.varta.data.config

import android.content.ContentValues.TAG
import android.util.Log
import com.devvikram.varta.data.firebase.models.FContact
import com.devvikram.varta.data.firebase.models.conversation.Conversation
import com.devvikram.varta.data.firebase.models.conversation.Participant
import com.devvikram.varta.data.firebase.models.conversation.UserPreference
import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.devvikram.varta.data.firebase.models.message.ChatMessage
import com.devvikram.varta.data.firebase.models.message.ForwardMetadata
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.models.RoomConversation
import com.devvikram.varta.data.room.models.RoomForwardMetadata
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.data.room.models.RoomUserPreference
import java.text.DateFormat
import java.util.Date
import java.util.Locale


object ModelMapper {
    fun convertTimestampToDateString(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
        return dateFormat.format(date)
    }

    // 1. Conversation to RoomConversation
    fun mapToRoomConversation(conversation: Conversation,currentUserId : String): RoomConversation {

        val userId = conversation.participantIds.first { it != currentUserId.toString() }
        Log.d(TAG, "mapToRoomConversation: ${conversation.participantIds}")

        return RoomConversation(
            conversationId = conversation.conversationId,
            type = conversation.type,
            groupType = conversation.groupType,
            name = conversation.name,
            createdBy = conversation.createdBy,
            createdAt = conversation.createdAt ?: 0L,
            description = conversation.description,
            participantIds = conversation.participantIds,
            userId = userId,
            lastModifiedAt = conversation.lastModifiedAt
        )
    }

    // 2. RoomConversation to Conversation
    fun mapToConversation(roomConversation: RoomConversation, participants: List<Participant>): Conversation {


        return Conversation(
            conversationId = roomConversation.conversationId,
            type = roomConversation.type,
            groupType = roomConversation.groupType,
            name = roomConversation.name,
            createdBy = roomConversation.createdBy,
            createdAt = roomConversation.createdAt,
            description = roomConversation.description,
            participants =participants,
            participantIds = roomConversation.participantIds ,// Participant IDs will be handled separately
            lastModifiedAt = roomConversation.lastModifiedAt
        )
    }

    // 3. ChatMessage to RoomMessage
    fun mapToRoomMessage(chatMessage: ChatMessage): RoomMessage {
        return RoomMessage(
            messageId = chatMessage.messageId,
            conversationId = chatMessage.conversationId,
            senderId = chatMessage.senderId,
            senderName = chatMessage.senderName,
            messageType = chatMessage.messageType.name,
            text = chatMessage.text,
            timestamp = chatMessage.timestamp ?: 0L,
            mediaUrl = chatMessage.mediaUrl,
            thumbnailUrl = chatMessage.thumbnailUrl,
            mediaSize = chatMessage.mediaSize,
            mediaDurationInSeconds = chatMessage.mediaDurationInSeconds,
            isEdited = chatMessage.isEdited,
            reactions = chatMessage.reactions.mapValues { it.value },
            isReadBy = chatMessage.isReadBy.mapValues { it.value ?: 0L },
            isReceivedBy = chatMessage.isReceivedBy.mapValues { it.value ?: 0L },
            mentions = chatMessage.mentions,
            replyToMessageId = chatMessage.replyToMessageId,
            deletedForUsers = chatMessage.deletedForUsers,
            isDeleted = chatMessage.isDeleted,
            isForwarded = chatMessage.isForwarded,
            forwardMetadata = chatMessage.forwardMetadata?.let {
                RoomForwardMetadata(
                    originalMessageId = it.originalMessageId,
                    originalSenderId = it.originalSenderId,
                    originalSenderName = it.originalSenderName,
                    originalConversationId = it.originalConversationId,
                    originalTimestamp = it.originalTimestamp ?: 0L,
                    forwarderChain = it.forwarderChain
                )
            },
            forwardCount = chatMessage.forwardCount,
            localFilePath = null, // Placeholder for downloaded file path
            isDownloaded = false,
            mediaType = chatMessage.mediaType,
            mediaName = chatMessage.mediaName,
        )
    }

    // 4. RoomMessage to ChatMessage
    fun mapToChatMessage(roomMessage: RoomMessage): ChatMessage {
        return ChatMessage(
            messageId = roomMessage.messageId,
            conversationId = roomMessage.conversationId,
            senderId = roomMessage.senderId,
            senderName = roomMessage.senderName,
            messageType = MessageType.valueOf(roomMessage.messageType),
            text = roomMessage.text,
            timestamp = roomMessage.timestamp,
            mediaUrl = roomMessage.mediaUrl,
            thumbnailUrl = roomMessage.thumbnailUrl,
            mediaSize = roomMessage.mediaSize,
            mediaDurationInSeconds = roomMessage.mediaDurationInSeconds,
            isEdited = roomMessage.isEdited,
            reactions = roomMessage.reactions.mapValues { it.value },
            isReadBy = roomMessage.isReadBy.mapValues { it.value ?: 0L },
            isReceivedBy = roomMessage.isReceivedBy.mapValues { it.value ?: 0L },
            mentions = roomMessage.mentions,
            replyToMessageId = roomMessage.replyToMessageId,
            deletedForUsers = roomMessage.deletedForUsers,
            isDeleted = roomMessage.isDeleted,
            isForwarded = roomMessage.isForwarded,
            forwardMetadata = roomMessage.forwardMetadata?.let {
                ForwardMetadata(
                    originalMessageId = it.originalMessageId,
                    originalSenderId = it.originalSenderId,
                    originalSenderName = it.originalSenderName,
                    originalConversationId = it.originalConversationId,
                    originalTimestamp = it.originalTimestamp,
                    forwarderChain = it.forwarderChain
                )
            },
            forwardCount = roomMessage.forwardCount,
            mediaType = roomMessage.mediaType,
            mediaName = roomMessage.mediaName
        )
    }

    // 5. Participant to RoomParticipant
    fun mapToRoomParticipant(participant: Participant, conversationId: String): RoomParticipant {
        return RoomParticipant(
            localParticipantId = 0, // Placeholder for Room database
            userId = participant.userId,
            conversationId = conversationId,
            role = participant.role
        )
    }

    // 6. RoomParticipant to Participant
    fun mapToParticipant(roomParticipant: RoomParticipant): Participant {
        return Participant(
            userId = roomParticipant.userId,
            role = roomParticipant.role ?: "MEMBER"
        )
    }

    // 7. UserPreference to RoomUserPreference
    fun mapToRoomUserPreference(
        userPreference: UserPreference,
        userId: String,
        conversationId: String
    ): RoomUserPreference {
        return RoomUserPreference(
            localUserPreferenceId = 0, // Placeholder for Room database
            conversationId = conversationId,
            isPinned = userPreference.isPinned,
            customNotificationTone = userPreference.customNotificationTone
        )
    }

    // 8. RoomUserPreference to UserPreference
    fun mapToUserPreference(roomUserPreference: RoomUserPreference): UserPreference {
        return UserPreference(
            isPinned = roomUserPreference.isPinned,
            customNotificationTone = roomUserPreference.customNotificationTone
        )
    }

    fun mapToFContact(fContact: FContact): ProContacts {
        return ProContacts(
            userId = fContact.userId,
            name = fContact.name ?: "",
            email = fContact.email ?: "",
            gender = fContact.gender ?: "",
            designation = fContact.designation ?: "",
            profilePic = fContact.profilePic ?: "",
            userStatus = fContact.userStatus ?: false,
            localProfilePicPath = fContact.localProfilePicPath ?: ""
        )
    }
}