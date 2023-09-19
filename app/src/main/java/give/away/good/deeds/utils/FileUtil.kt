package give.away.good.deeds.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.util.Locale

object FileUtil {

    fun getImageUri(context: Context): Uri {
        val directory = File(context.cacheDir, "images")
        directory.mkdirs()
        val file = File.createTempFile(
            "image_",
            ".jpg",
            directory,
        )
        val authority = context.packageName + ".provider"
        return FileProvider.getUriForFile(
            context,
            authority,
            file,
        )
    }

    fun getMimeType(context: Context, uri: Uri): String? {
        return when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> context.contentResolver.getType(uri)
            ContentResolver.SCHEME_FILE -> MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(uri.toString()).lowercase()
            )

            else -> null
        }
    }

    fun getFileName(context: Context, uri: Uri): String? {
        when (uri.scheme) {
            ContentResolver.SCHEME_FILE -> {
                val filePath = uri.path
                if (!filePath.isNullOrEmpty()) {
                    return File(filePath).name
                }
            }

            ContentResolver.SCHEME_CONTENT -> {
                return getCursorContent(uri, context.contentResolver)
            }
        }

        return null
    }

    private fun getCursorContent(uri: Uri, contentResolver: ContentResolver): String? =
        kotlin.runCatching {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst()) {
                    cursor.getString(nameColumnIndex)
                } else null
            }
        }.getOrNull()


}