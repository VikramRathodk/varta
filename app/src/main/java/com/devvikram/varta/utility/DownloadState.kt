package com.devvikram.varta.utility

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class DownloadState {
    Initial,
    Downloading,
    Completed,
    Error
}

fun simulateDownload(onComplete: (Boolean) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        delay(3000)
        onComplete(false)
    }
}