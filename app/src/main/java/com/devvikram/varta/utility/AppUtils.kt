package com.devvikram.varta.utility

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.MediaType
import com.devvikram.varta.data.firebase.models.enums.MessageType
import com.devvikram.varta.data.room.models.ProContacts
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem
import com.google.firebase.BuildConfig
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale



class AppUtils {
    companion object
    {
        fun getAppVersion(): String {
            return BuildConfig.VERSION_NAME
        }
        fun downloadImage(url: String): Bitmap? {
            return try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                Log.e("AppUtils", "Error downloading image: ${e.message}")
                null // Return null if the image cannot be downloaded
            }
        }

        fun saveImageToStorage(context: Context, bitmap: Bitmap, contact: ProContacts): String {
            val userId = contact.userId.toString()
            val userDir = File(context.filesDir, userId)

            if (!userDir.exists()) {
                userDir.mkdirs()
            }

            val safeFileName = contact.name.replace(" ", "_")
                .replace(".", "")
                .lowercase(Locale.getDefault())
            val imageFile = File(userDir, "$safeFileName.png")

            try {
                imageFile.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

            return imageFile.absolutePath
        }



        fun <T>copyChatMessageContent(context: Context,selectedChatMessageList: List<T>) {
            if (selectedChatMessageList.isEmpty()) {
                Toast.makeText(context, "No messages selected to copy.", Toast.LENGTH_SHORT).show()
                return
            }
            val messagesToCopy = selectedChatMessageList.joinToString(separator = "\n\n") {
                when (it) {
                    is PersonalChatMessageItem -> it.content
                    is GroupChatMessageItem -> it.content
                    else -> ""
                }
            }

            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clip = ClipData.newPlainText("Copied Messages", messagesToCopy)
            clipboardManager.setPrimaryClip(clip)

            Toast.makeText(context, "Messages copied to clipboard.", Toast.LENGTH_SHORT).show()
        }

        fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
            // Define the target width and height
            val targetWidth = 1000
            val targetHeight = 1200

            val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

            var newWidth = targetWidth
            var newHeight = (targetWidth / aspectRatio).toInt()

            if (newHeight > targetHeight) {
                newHeight = targetHeight
                newWidth = (targetHeight * aspectRatio).toInt()
            }

            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)

            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            val imageFile = File.createTempFile("image", ".jpg", storageDir)

            val out = imageFile.outputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()

            return Uri.fromFile(imageFile)
        }


        fun getFileExtension(context: Context, uri: Uri): String? {
            return when (uri.scheme) {
                "content" -> {
                    val mimeType = context.contentResolver.getType(uri)
                    mimeType?.let {
                        MimeTypeMap.getSingleton().getExtensionFromMimeType(it) ?: "unknown"
                    }
                }
                "file" -> {
                    uri.path?.let { path ->
                        val extension = File(path).extension
                        if (extension.isNotEmpty()) extension.lowercase() else "unknown"
                    } ?: "unknown"
                }
                "http", "https" -> {
                    uri.path?.substringAfterLast("/")?.substringAfterLast(".")?.lowercase() ?: "unknown"
                }
                else -> {
                    null
                }
            }
        }

        fun getMediaType(context: Context,uri: Uri): MediaType {
            val extension = getFileExtension(context, uri)
            return when (extension?.lowercase()) {
                "jpg", "jpeg", "png" -> MediaType.IMAGE
                "pdf" -> MediaType.PDF
                "doc", "docx" -> MediaType.WORD
                "xls", "xlsx" -> MediaType.EXCEL
                "ppt", "pptx" -> MediaType.PPT
                "zip" -> MediaType.ZIP
                "tar" -> MediaType.TAR
                "rar" -> MediaType.RAR
                "mp4" -> MediaType.VIDEO
                "mp3" -> MediaType.AUDIO
                "txt" -> MediaType.DOCUMENT
                "dxf", "dwf", "dwg" -> MediaType.AUTOCAD
                else -> MediaType.OTHER
            }
        }

        fun findImageSize(selectedMediaUri: Uri?, context: Context): Pair<Int, Int>? {
            return try {
                when (selectedMediaUri?.scheme) {
                    "content" -> {
                        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                        context.contentResolver.openInputStream(selectedMediaUri)?.use { inputStream ->
                            BitmapFactory.decodeStream(inputStream, null, options)
                        }
                        options.outWidth to options.outHeight
                    }
                    "file" -> {
                        val file = selectedMediaUri.path?.let { File(it) }
                        if (file != null && file.exists()) {
                            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                            BitmapFactory.decodeFile(file.absolutePath, options)
                            options.outWidth to options.outHeight
                        } else null
                    }
                    "http", "https" -> {
                        val url = URL(selectedMediaUri.toString())
                        val connection = url.openConnection() as HttpURLConnection
                        connection.doInput = true
                        connection.connect()
                        connection.inputStream.use { inputStream ->
                            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                            BitmapFactory.decodeStream(inputStream, null, options)
                            options.outWidth to options.outHeight
                        }
                    }
                    else -> null
                }
            } catch (e: Exception) {
                null
            }
        }

        fun getFileName(selectedMediaUri: Uri?,context: Context): String {
            return when (selectedMediaUri?.scheme) {
                "content" -> {
                    val fileName = selectedMediaUri.lastPathSegment?: "Unknown"
                    val extension = getFileExtension(context, selectedMediaUri)
                    "$fileName.${extension?.lowercase()?: "unknown"}"
                }
                "file" -> {
                    val file = selectedMediaUri.path?.let { File(it) }
                    file?.name?: "Unknown"
                }
                "http", "https" -> {
                    val url = URL(selectedMediaUri.toString())
                    url.path?.substringAfterLast("/")?: "Unknown"
                }
                else -> "Unknown"
            }
        }

        fun getTimeInHoursAndMinutes(timestamp: Long): String {
            val date = Date(timestamp)
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault()) // 24-hour format
            return formatter.format(date)
        }

        fun getFormattedDate(): String {
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            return formatter.format(Date())
        }

        fun getMessageType(messageType: String): MessageType {
            return when (messageType) {
                "TEXT" -> MessageType.TEXT
                "IMAGE" -> MessageType.IMAGE
                "video" -> MessageType.VIDEO
                "audio" -> MessageType.AUDIO
                "location" -> MessageType.LOCATION
                else -> MessageType.OTHER
            }
        }
        fun getDateRange(): List<String> {
            val dateList = mutableListOf<String>()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            calendar.add(Calendar.DATE, -1)

            for (i in 0 until 7) {
                dateList.add(dateFormat.format(calendar.time))
                calendar.add(Calendar.DATE, -1)
            }
            return dateList
        }

        fun getDateFromTimestamp(timeStamp: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        fun getMimeType(context: Context, fileUri: Uri?): String {
            if (fileUri == null) return "text/plain"

            // First, try to get MIME type from content resolver
            val contentResolverMimeType = context.contentResolver.getType(fileUri)
            if (contentResolverMimeType != null) return contentResolverMimeType

            // If content resolver fails, use file extension as a fallback
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString())
            return if (!fileExtension.isNullOrEmpty()) {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension) ?: "application/octet-stream"
            } else {
                "application/octet-stream" // Default fallback
            }
        }


        fun getIconFromMimeType(mimeType: String): Int {
            return when (mimeType) {
                // **Google Docs**
                "application/vnd.google-apps.document" -> R.drawable.doc_icon

                // **Microsoft Word**
//                TODO change the icon later on with excel icon

                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> R.drawable.doc_icon


                // **Microsoft Excel**
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> R.drawable.excel_icon

//               // **Microsoft PowerPoint**
//               "application/vnd.ms-powerpoint",
//               "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> R.drawable.ppt_icon

                // **Google Sheets**
//                "application/vnd.google-apps.spreadsheet" -> R.drawable.sheets_icon

                // **Google Slides**
//               "application/vnd.google-apps.presentation" -> R.drawable.google_slides_icon

                // **PDF**
                "application/pdf" -> R.drawable.pdf_icon

                // **Image Files**
                "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp", "image/tiff", "image/svg+xml" -> R.drawable.image_icon

                // **Video Files**
                "video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo", "video/x-matroska", "video/webm" -> R.drawable.video_icon

                // **Audio Files**
                "audio/mpeg", "audio/wav", "audio/ogg", "audio/aac", "audio/mp3" -> R.drawable.audio_icon

//                // **Compressed Files**
//                "application/zip", "application/x-rar-compressed", "application/x-7z-compressed", "application/x-tar", "application/gzip" -> R.drawable.baseline_unarchive_24
//
//                // **Text Files**
//                "text/plain", "text/csv", "text/html", "application/json", "application/xml" -> R.drawable.text_icon

                "application/octet-stream" -> R.drawable.autocad

                // **Default Document Icon**
                else -> R.drawable.document_file_icon
            }
        }

        fun openAnyFile(context: Context, filePath: String, fileType: String) {
            Log.d(TAG, "openAnyFile: filepath $filePath, fileType: $fileType")

            val uri: Uri = if (filePath.startsWith("content://")) {
                Uri.parse(filePath)
            } else {
                val file = File(filePath)
                FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, fileType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "No app found to open this file type.", Toast.LENGTH_LONG).show()
            }
        }

    }
}