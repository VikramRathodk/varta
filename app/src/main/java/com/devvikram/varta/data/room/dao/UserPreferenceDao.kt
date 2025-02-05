package com.devvikram.varta.data.room.dao

import androidx.room.*
import com.devvikram.varta.data.room.models.RoomUserPreference

@Dao
interface UserPreferenceDao {
    @Upsert
    suspend fun insertUserPreference(userPreference: RoomUserPreference)

    @Upsert
    suspend fun insertUserPreferences(userPreferences: List<RoomUserPreference>)
}