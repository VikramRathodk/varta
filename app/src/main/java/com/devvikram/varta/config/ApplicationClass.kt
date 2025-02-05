package com.devvikram.varta.config

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.devvikram.varta.config.constants.LoginPreference
import com.devvikram.varta.data.room.repository.ContactRepository
import com.devvikram.varta.workers.TestWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: CustomWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setExecutor(Executors.newFixedThreadPool(3))
            .setTaskExecutor(Executors.newFixedThreadPool(2))
            .build()

    override fun onCreate() {
        super.onCreate()
    }
}

class CustomWorkerFactory @Inject constructor(
    private val contactRepository: ContactRepository,
    private val loginPreference: LoginPreference
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = TestWorker(
        appContext = appContext,
        workerParams = workerParameters,
        contactRepository = contactRepository,
        loginPreference = loginPreference
    )

}