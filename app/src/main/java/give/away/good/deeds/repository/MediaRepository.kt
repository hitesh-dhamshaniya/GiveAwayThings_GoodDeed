package give.away.good.deeds.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

/**
 * MediaRepository
 * @author Hitesh
 * @since 02.09.2023
 */
interface MediaRepository {

    suspend fun uploadProfileImage(userId: String, uri: Uri): String

    suspend fun uploadPostImages(postId: String, images: List<Uri>): List<String>

}

/**
 * MediaRepositoryImp
 * @author Hitesh
 * @since 02.09.2023
 */
class MediaRepositoryImpl(
    private val context: Context,
    private val storage: FirebaseStorage,
) : MediaRepository {

    override suspend fun uploadProfileImage(userId: String, uri: Uri): String {
        val fileName = "$userId.jpg"
        val fileRef = storage.reference.child("users").child(fileName)
        return uploadUri(context, uri, fileRef)
    }

    override suspend fun uploadPostImages(postId: String, images: List<Uri>): List<String> {
        var index = 1
        return images.map { uri ->
            val fileName = "$postId-${index++}.jpg"
            val fileRef = storage.reference.child("posts").child(postId).child(fileName)
            uploadUri(context, uri, fileRef)
        }
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