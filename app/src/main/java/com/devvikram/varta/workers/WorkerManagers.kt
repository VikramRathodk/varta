package com.devvikram.varta.workers

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkerManagers private constructor(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: WorkerManagers? = null

        fun getInstance(context: Context): WorkerManagers {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WorkerManagers(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun syncContacts() {
        val workRequest = OneTimeWorkRequestBuilder<TestWorker>()
            .build()

        WorkManager.getInstance(context)
            .enqueue(workRequest)
    }
}

