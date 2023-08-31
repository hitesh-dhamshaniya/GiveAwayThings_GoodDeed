package give.away.good.deeds.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import give.away.good.deeds.utils.FileUtil
import kotlinx.coroutines.tasks.await

interface MediaRepository {

    suspend fun uploadProfileImage(context: Context, userId: String, uri: Uri): String

}

class MediaRepositoryImpl(
    private val storage: FirebaseStorage,
) : MediaRepository {

    override suspend fun uploadProfileImage(context: Context, userId: String, uri: Uri): String {
        val fileName = FileUtil.getFileName(context, uri) ?: "profile.jpg"
        val fileRef = storage.reference
            .child("users")
            .child(userId)
            .child("profile")
            .child(fileName)
        return uploadUri(context, uri, fileRef)
    }

    private suspend fun uploadUri(context: Context, uri: Uri, fileRef: StorageReference): String {
        val uploadTask = context.contentResolver.openInputStream(uri)?.let { stream ->
            fileRef.putStream(stream)
        }

        // finish image upload
        uploadTask?.await()

        // get download url
        val downloadUri: Uri = fileRef.downloadUrl.await()

        // convert to string
        return downloadUri.toString()
    }
}