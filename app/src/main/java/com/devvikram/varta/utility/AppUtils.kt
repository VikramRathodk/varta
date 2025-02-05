package com.devvikram.varta.utility

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.devvikram.varta.config.constants.MediaType
import com.devvikram.varta.ui.screens.groupchatroom.chatroom.models.GroupChatMessageItem
import com.devvikram.varta.ui.itemmodels.PersonalChatMessageItem
import com.google.firebase.BuildConfig
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppUtils {
   companion object
   {
       fun getAppVersion(): String {
           return BuildConfig.VERSION_NAME
       }
       fun downloadImage(url: String): Bitmap {
           val connection = URL(url).openConnection() as HttpURLConnection
           connection.doInput = true
           connection.connect()
           return BitmapFactory.decodeStream(connection.inputStream)
       }

       fun saveImageToStorage(bitmap: Bitmap, fileName: String): String {
           val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
           val imageFile = File(storageDir, "$fileName.jpg")
           val out = imageFile.outputStream()
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
           out.close()
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

       fun getFileName(selectedMediaUri: Uri?,context: Context): Any {
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

   }
}