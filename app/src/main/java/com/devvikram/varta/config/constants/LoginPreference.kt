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

    fun setIsLoggedIn(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(LOGGEDIN, boolean).apply()
    }


    companion object {
        const val SHARED_PREF_NAME = "SHARED_PREF_NAME"
        const val LOGIN_SESSION_ID = "LOGIN_SESSION_ID"
        const val USER_ID = "userId"
        const val LOGIN_SESSION_STATUS = "LOGIN_SESSION_STATUS"
        const val LOGGEDIN = "isLoggedIn"
    }
}