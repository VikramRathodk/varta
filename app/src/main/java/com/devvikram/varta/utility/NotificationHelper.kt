package com.devvikram.varta.utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.devvikram.varta.AppActivity
import com.devvikram.varta.R
import com.devvikram.varta.config.App
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.devvikram.varta.data.firebase.models.message.ChatMessage
import com.devvikram.varta.data.room.models.ConversationWithContact
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.utility.AppUtils.Companion.getMessageType

import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext  private val  context : Context,
    private val notificationManager: NotificationManager,
    private val conversationRepository: ConversationRepository,
    private val loginPreference: LoginPreference,
) {

    private val notificationIdCounter = AtomicInteger(0)

    // Channels for personal converations and group conversations

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                id = App.PERSONAL_MESSAGE_NOTIFICATION_CHANNEL_ID,
                name = App.PERSONAL_MESSAGE_NOTIFICATION_CHANNEL_NAME,
                descriptionText = "Channel for personal messages"
            )
            createNotificationChannel(
                id = App.GROUP_MESSAGE_NOTIFICATION_CHANNEL_ID,
                name = App.GROUP_MESSAGE_NOTIFICATION_CHANNEL_NAME,
                descriptionText = "Channel for group messages"
            )
            createNotificationChannel(
                id = App.SECURITY_CODE_NOTIFICATION_CHANNEL_ID,
                name = App.SECURITY_CODE_NOTIFICATION_CHANNEL_NAME,
                descriptionText = "Channel for system messages"
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String,
        name: String,
        descriptionText: String
    ) {
        val channel = NotificationChannel(
            id,
            name,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = descriptionText
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(
        title: String,
        body: String,
        conversationId: String?,
        conversationType: String?,
        chatMessage: ChatMessage?
    ) {
        when (conversationType) {
            "P" -> {
                if (conversationId != null) {
                    createOrUpdatePersonalChatNotification(
                        title = title,
                        body = body,
                        conversationKey = conversationId,
                        chatMessage = chatMessage
                    )
                }
            }
            "G" -> {
                // Create or Update Group  Notification
                createOrUpdateGroupNotification(
                    title = title,
                    body = body,
                    conversationId = conversationId,
                    chatMessage = chatMessage
                )
            }
        }
    }
    private fun createOrUpdatePersonalChatNotification(title: String, body: String, conversationKey: String, chatMessage: ChatMessage?) {
        Log.d(TAG, "createOrUpdatePersonalChatNotification: Title: $title, Body: $body, ConversationId: $conversationKey, ChatMessage: $chatMessage")

        val existingNotification =
            notificationManager.activeNotifications.find { it.tag == conversationKey }
        val messageType = chatMessage?.messageType.toString()
        var messageBody = chatMessage?.text.toString()

        if (messageType != "TEXT") {
            messageBody = "${getMessageType(messageType)} Shared a media file with you"
        }

        CoroutineScope(Dispatchers.IO).launch {
            val conversationWithContact =
                conversationRepository.getConversationAndContactWithConversationUniqueKey(
                    conversationKey
                )
            val proUserId = conversationWithContact.contact?.userId ?: 0
            val conversationId = conversationWithContact.conversation.conversationId ?: 0

            Log.d(TAG, "createOrUpdatePersonalChatNotification: ConversationWithContact: $conversationWithContact")

            val navigateIntent = getChatPendingIntent(context,conversationId.toString(),conversationWithContact, currentUserId = loginPreference.getUserId().toLong())

            withContext(Dispatchers.Main) {
                val notificationId = existingNotification?.id ?: conversationKey.hashCode()

                if (existingNotification != null) {
                    // Update existing notification
                    val existingMessagingStyle =
                        NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
                            existingNotification.notification
                        )
                    val senderPerson = Person.Builder()
                        .setName(chatMessage?.senderName.toString())
                        .build()

                    existingMessagingStyle?.addMessage(
                        messageBody,
                        System.currentTimeMillis(),
                        senderPerson
                    )

                    val notification = NotificationCompat.Builder(
                        context,
                        App.PERSONAL_MESSAGE_NOTIFICATION_CHANNEL_ID
                    )
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setSmallIcon(R.drawable.varta_logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setStyle(existingMessagingStyle)
                        .setContentIntent(navigateIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setGroup("ProChat")
                        .setSubText("ProChat Message Notification")
                        .build()

                    notificationManager.notify(conversationKey, notificationId, notification)
                } else {
                    // Create new notification
                    val senderPerson = Person.Builder()
                        .setName(chatMessage?.senderName.toString())
                        .build()

                    val messagingStyle = NotificationCompat.MessagingStyle(senderPerson)
                        .addMessage(
                            messageBody,
                            System.currentTimeMillis(),
                            senderPerson
                        )

                    val notification = NotificationCompat.Builder(
                        context,
                        App.PERSONAL_MESSAGE_NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.prochat_logo)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setStyle(messagingStyle)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setGroup("ProChat")
                        .setSubText("ProChat Message Notification")
                        .build()

                    notificationManager.notify(conversationKey, notificationId, notification)
                }
            }
        }
    }


    private fun getChatPendingIntent(
        context: Context,
        conversationId: String?,
        conversationWithContact: ConversationWithContact,
        currentUserId: Long = 0
    ): PendingIntent? {


        val receiverId = conversationWithContact.conversation.participantIds.first { it.toLong() != currentUserId }
        Log.d(TAG, "getChatPendingIntent:  current user id : $currentUserId  participant sid s are : ${conversationWithContact.conversation.participantIds} ReceiverId: $receiverId")

        val intent = Intent(context, AppActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra("conversationId", conversationId)
            putExtra("receiverId", receiverId)
            putExtra("conversationType", conversationWithContact.conversation.type)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context, conversationId.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }




    private fun createOrUpdateGroupNotification(
        title: String,
        body: String,
        conversationId: String?,
        chatMessage: ChatMessage?,
    ) {
        // Create Message Grouping previously and new one in same notification
        Log.d(TAG, "createOrUpdateGroupNotification: Title: $title, Body: $body, ConversationId: $conversationId, ChatMessage: $chatMessage")
        // TODO: Implement logic to create or update group notification based on existing conversationId and chatMessage
        val existingNotification = notificationManager.activeNotifications.find {
            it.tag == conversationId.toString()
        }

        val messageType = chatMessage?.messageType.toString()

        var messageBody = body
        if (messageType != MessageType.TEXT.toString()) {
            messageBody = "${getMessageType(messageType)} Shared a media file with you"
        }

        if (existingNotification != null) {
//            Existing Notifications
            CoroutineScope(Dispatchers.IO).launch {

                val conversationWithContact = conversationId?.let {
                    conversationRepository.getConversationAndContactWithConversationUniqueKey(
                        it
                    )
                }

                withContext(Dispatchers.Main) {
                    val existingMessagingStyle =
                        NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
                            existingNotification.notification
                        )

                    val person =
                        Person.Builder().setName(chatMessage?.senderName.toString())
                            .build()

                    existingMessagingStyle?.addMessage(
                        messageBody,
                        System.currentTimeMillis(),
                        person
                    )

                    val notification = NotificationCompat.Builder(
                        context,
                        App.GROUP_MESSAGE_NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.prochat_logo)
                        .setContentTitle(conversationWithContact?.conversation?.name)
                        .setContentText(messageBody).setStyle(existingMessagingStyle)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentIntent(conversationWithContact?.let {
                            getChatPendingIntent(
                                context = context,
                                currentUserId = loginPreference.getUserId().toLong(),
                                conversationId = conversationId.toString(),
                                conversationWithContact = it
                            )
                        }).build()

                    val existingNotificationId = existingNotification.id
                    Log.d(
                        TAG,
                        "handleGroupNotification: existing Group notification Attach : $existingNotification"
                    )


                    notificationManager.notify(
                        conversationId.toString(),
                        existingNotificationId,
                        notification
                    )
                }

            }

        } else {
            //            New Group Notification
            CoroutineScope(Dispatchers.IO).launch {
                val conversationWithContact = conversationId?.let {
                    conversationRepository.getConversationAndContactWithConversationUniqueKey(
                        it
                    )
                }

                withContext(Dispatchers.Main) {
                    val conversation =
                        Person.Builder().setName(conversationWithContact?.conversation?.name).build()

                    val person =
                        Person.Builder().setName(chatMessage?.senderName.toString())
                            .build()

                    val messagingStyle = NotificationCompat.MessagingStyle(conversation)
                        .setConversationTitle(conversationWithContact?.conversation?.name)
                        .addMessage(messageBody, Date().time, person)


                    val notification = NotificationCompat.Builder(
                        context,
                        App.GROUP_MESSAGE_NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.prochat_logo)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .setContentTitle(conversationWithContact?.conversation?.name)
                        .setContentText(messageBody)
                        .setStyle(messagingStyle)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setContentIntent(
                            conversationWithContact?.let {
                                getChatPendingIntent(
                                    context = context,
                                    currentUserId = loginPreference.getUserId().toLong(),
                                    conversationId = conversationId.toString(),
                                    conversationWithContact = it
                                )
                            }
                        ).build()

                    Log.d(TAG, "handleGroupNotification: Group notification New : $notification")

                    notificationManager.notify(
                        conversationId,
                        System.currentTimeMillis().toInt(),
                        notification
                    )
                }
            }
        }
    }


    fun showSecurityCodeNotification( securityData: String?) {
        Log.d(TAG, "showSecurityCodeNotification: $securityData")

        val notificationId = notificationIdCounter.incrementAndGet()

        val notification = NotificationCompat.Builder(
            context,
            App.SECURITY_CODE_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.prochat_logo)
            .setContentTitle("Security Code")
            .setContentText(securityData ?: "No security code available")
            .setContentIntent(securityIntent(securityData))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }


    private fun securityIntent(securityData: String?): PendingIntent? {
        val intent = Intent(context, AppActivity::class.java).apply {
            putExtra("securityCode", securityData)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    fun showLogoutNotification() {

        loginPreference.clearSharedPref()
        loginPreference.setIsLoggedIn(false)

        val notificationId = notificationIdCounter.incrementAndGet()

        val notification = NotificationCompat.Builder(
            context,
            App.SECURITY_CODE_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.prochat_logo)
            .setContentTitle("Logout")
            .setContentText("You have been logged out.")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}