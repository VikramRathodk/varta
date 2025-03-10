package com.devvikram.varta.data.firebase

import com.devvikram.varta.config.constants.LoginPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService @Inject constructor(
    private val loginPreferences: LoginPreference,
): FirebaseMessagingService() {


    override fun onNewToken(newToken: String) {
        println("Refreshed FCM Token: $newToken")
        loginPreferences.setFirebaseToken(newToken)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("Message received: $remoteMessage")

    }
}
