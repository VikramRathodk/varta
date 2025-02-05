package com.devvikram.varta.data.room.repository

import com.devvikram.varta.data.room.dao.UserPreferenceDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferenceRepository @Inject constructor(private val userPreferenceDao: UserPreferenceDao) {
}