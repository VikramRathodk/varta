package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R
import com.devvikram.varta.utility.DownloadState
import kotlinx.coroutines.delay

@Composable
fun DownloadStateIcon(
    downloadState: DownloadState,
    onDownloadClick: () -> Unit,
    onRetryDownload: () -> Unit
) {
    var showCompletionIcon by remember { mutableStateOf(false) }

    LaunchedEffect(downloadState) {
        if (downloadState == DownloadState.Completed) {
            showCompletionIcon = true
            delay(2000)
            showCompletionIcon = false
        }
    }

    when (downloadState) {
        DownloadState.Initial -> {
            Icon(
                painter = painterResource(id = R.drawable.download_icon),
                contentDescription = "Download",
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable { onDownloadClick() },
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DownloadState.Downloading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        DownloadState.Completed -> {
            if (showCompletionIcon) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_24),
                    contentDescription = "Download Completed",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        DownloadState.Error -> {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Download Error",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRetryDownload() },
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
