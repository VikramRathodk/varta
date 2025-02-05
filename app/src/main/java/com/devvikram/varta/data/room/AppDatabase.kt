package com.devvikram.varta.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devvikram.varta.config.App
import com.devvikram.varta.data.room.converters.Converters
import com.devvikram.varta.data.room.dao.ConversationDao
import com.devvikram.varta.data.room.dao.MessageDao
import com.devvikram.varta.data.room.dao.ParticipantDao
import com.devvikram.varta.data.room.dao.UserContactsDao
import com.devvikram.varta.data.room.dao.UserPreferenceDao
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.data.room.models.RoomConversation
import com.devvikram.varta.data.room.models.RoomMessage
import com.devvikram.varta.data.room.models.RoomParticipant
import com.devvikram.varta.data.room.models.RoomUserPreference

@Database(
    entities = [
        RoomConversation::class,
        RoomParticipant::class,
        RoomMessage::class,
        RoomUserPreference::class,
        ProContacts::class
    ],
    version = App.DATABASE_CURRENT_VERSION,
    exportSchema = true
)
@AutoMigration(from = App.DATABASE_LAST_VERSION, to = App.DATABASE_CURRENT_VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun participantDao(): ParticipantDao
    abstract fun userPreferenceDao(): UserPreferenceDao
    abstract fun userContactsDao(): UserContactsDao
}