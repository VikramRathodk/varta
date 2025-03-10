package com.devvikram.varta.config.constants

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class LoginPreference @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun getLoginSessionId(): String {
        return sharedPreferences.getString(LOGIN_SESSION_ID, "").toString()
    }

    fun getUserId(): String {
        return sharedPreferences.getString(USER_ID, "").toString()
    }

    fun getLoginSessionStatus(): Boolean {
        return sharedPreferences.getBoolean(LOGIN_SESSION_STATUS, false)
    }

    fun getIsLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(LOGGEDIN, false)
    }

    fun saveLoginInfo(userId: String, loginSessionId: String, loginSessionStatus: Boolean){
        sharedPreferences.edit().apply{
            putString(USER_ID, userId)
            putString(LOGIN_SESSION_ID, loginSessionId)
            putBoolean(LOGIN_SESSION_STATUS, loginSessionStatus)
            putBoolean(LOGGEDIN, true)
            apply()
        }

    }
    fun clearSharedPref(){
        sharedPreferences.edit().clear().apply()
    }
    fun getChatMessageSyncToLocalTimestamp(): Long {
        return sharedPreferences.getLong(CHAT_MESSAGE_SYNC_TO_LOCAL_TIMESTAMP, 0)
    }
    fun getConversationSyncToLocalTimestamp(): Long {
        return sharedPreferences.getLong(CONVERSATION_SYNC_TO_LOCAL_TIMESTAMP, 0)
    }

    fun setIsLoggedIn(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(LOGGEDIN, boolean).apply()
    }

    fun setFirebaseMessagingToken(token: String) {
        sharedPreferences.edit().putString(FIREBASE_MESSAGING_TOKEN, token).apply()
    }
    fun getFirebaseMessagingToken(): String {
        return sharedPreferences.getString(FIREBASE_MESSAGING_TOKEN, "").toString()
    }

    fun setCurrentlyActiveConversation(conversationId: String) {
        sharedPreferences.edit().putString(CURRENTLY_ACTIVE_CONVERSATION, conversationId).apply()
    }
    fun getCurrentlyActiveConversation(): String {
        return sharedPreferences.getString(CURRENTLY_ACTIVE_CONVERSATION, "").toString()
    }
    fun setFirstLaunch(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH, boolean).apply()
    }
    fun getFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true)
    }
    fun setAppOpenState(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(APP_OPEN_STATE, boolean).apply()
    }
    fun getAppOpenState(): Boolean {
        return sharedPreferences.getBoolean(APP_OPEN_STATE, false)
    }

    fun setDeviceUniqueId(value: String) {
        sharedPreferences.edit().putString(DEVICE_UNIQUE_ID, value).apply()
    }
    fun getDeviceUniqueId(): String {
        return sharedPreferences.getString(DEVICE_UNIQUE_ID, "").toString()
    }



    companion object {
        const val SHARED_PREF_NAME = "SHARED_PREF_NAME"
        const val LOGIN_SESSION_ID = "LOGIN_SESSION_ID"
        const val USER_ID = "userId"
        const val LOGIN_SESSION_STATUS = "LOGIN_SESSION_STATUS"
        const val LOGGEDIN = "isLoggedIn"
        const val CONVERSATION_SYNC_TO_LOCAL_TIMESTAMP = "CONVERSATION_SYNC_TO_LOCAL_TIMESTAMP"
        const val CHAT_MESSAGE_SYNC_TO_LOCAL_TIMESTAMP = "CHAT_MESSAGE_SYNC_TO_LOCAL_TIMESTAMP"
        const val FIREBASE_MESSAGING_TOKEN  = "FIREBASE_MESSAGING_TOKEN"
        const val CURRENTLY_ACTIVE_CONVERSATION = "CURRENTLY_ACTIVE_CONVERSATION"
        const val FIRST_LAUNCH = "FIRST_LAUNCH"
        const val APP_OPEN_STATE = "APP_OPEN_STATE"
        const val DEVICE_UNIQUE_ID = "DEVICE_UNIQUE_ID"
    }
}