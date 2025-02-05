package com.devvikram.varta.data.config

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.devvikram.varta.config.App
import com.devvikram.varta.data.firebase.repositories.FirebaseConversationRepository
import com.devvikram.varta.data.firebase.repositories.FirebaseMessageRepository
import com.devvikram.varta.data.room.AppDatabase
import com.devvikram.varta.data.room.dao.ConversationDao
import com.devvikram.varta.data.room.dao.MessageDao
import com.devvikram.varta.data.room.dao.ParticipantDao
import com.devvikram.varta.data.room.dao.UserContactsDao
import com.devvikram.varta.data.room.dao.UserPreferenceDao
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.data.room.repository.ConversationRepository
import com.devvikram.varta.data.room.repository.MessageRepository
import com.devvikram.varta.data.room.repository.ParticipantRepository
import com.devvikram.varta.data.room.repository.UserPreferenceRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /***********************************************************************************************
     *********************************** Provide Fire-store Repositories **************************/
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideFirebaseConversationRepository(
        firestore: FirebaseFirestore
    ): FirebaseConversationRepository {
        return FirebaseConversationRepository(firestore)
    }

    @Provides
    fun provideFirebaseMessageRepository(firestore: FirebaseFirestore, firebaseConversationRepository: FirebaseConversationRepository): FirebaseMessageRepository {
        return FirebaseMessageRepository(firestore = firestore, firebaseConversationRepository = firebaseConversationRepository)
    }


    /***********************************************************************************************
     *********************************** Provide Fire-store Repositories **************************/


    /************************************************************************************
     *********************************** Provide DAOs **********************************/

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            App.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideConversationDao(db: AppDatabase) = db.conversationDao()

    @Provides
    fun provideMessageDao(db: AppDatabase) = db.messageDao()

    @Provides
    fun provideParticipantDao(db: AppDatabase) = db.participantDao()

    @Provides
    fun provideUserPreferenceDao(db: AppDatabase) = db.userPreferenceDao()

    /************************************************************************************
     ********************************* Provide DAOs *************************************/

    /************************************************************************************
     ********************************* Provide Room Repositories *************************************/

    @Provides
    fun provideConversationRepository(
        conversationDao: ConversationDao,
        firestore: FirebaseFirestore,
        firebaseConversationRepository: FirebaseConversationRepository,
        participantRepository: ParticipantRepository,
        messageRepository: MessageRepository
    ): ConversationRepository {
        return ConversationRepository(
            conversationDao = conversationDao,
            firestore = firestore,
            firebaseConversationRepository = firebaseConversationRepository,
            participantRepository = participantRepository,
            messageRepository = messageRepository
        )
    }


    @Provides
    fun provideMessageRepository(messageDao: MessageDao,firestore: FirebaseFirestore,
                                 firebaseMessageRepository: FirebaseMessageRepository,
                                 firebaseConversationRepository: FirebaseConversationRepository
    ): MessageRepository {
        return MessageRepository(
            messageDao = messageDao,
            firebaseFirestore = firestore,
            firebaseMessageRepository = firebaseMessageRepository,
        )
    }

    @Provides
    fun provideParticipantRepository(participantDao: ParticipantDao,firebaseConversationRepository: FirebaseConversationRepository): ParticipantRepository {
        return ParticipantRepository(participantDao,firebaseConversationRepository)
    }

    @Provides
    fun provideUserPreferenceRepository(userPreferenceDao: UserPreferenceDao): UserPreferenceRepository {
        return UserPreferenceRepository(userPreferenceDao)
    }

    /************************************************************************************
     ********************************* Provide Room Repositories *************************************/

    @Provides
    @Singleton
    fun sharedPreference(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences(
        App.USER_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    @Provides
    fun provideContactDao(db: AppDatabase) = db.userContactsDao()

    @Provides
    fun provideContactRepository(userContactDao: UserContactsDao): ContactRepository {
        return ContactRepository(userContactDao)
    }



}
