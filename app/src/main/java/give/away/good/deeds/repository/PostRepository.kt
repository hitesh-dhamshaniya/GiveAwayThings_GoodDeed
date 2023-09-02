package give.away.good.deeds.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.network.model.toPost
import kotlinx.coroutines.tasks.await

private const val COLLECTION_POST: String = "posts"

interface PostRepository {

    suspend fun createPost(
        post: Post,
        images: List<Uri>
    ): CallResult<Unit>

    suspend fun getPost(postId: String): CallResult<Post>

    suspend fun getPost(): CallResult<List<Post>>

    suspend fun getMyPosts(): CallResult<List<Post>>

    suspend fun searchPost(query: String): CallResult<List<Post>>

    suspend fun updatePostStatus(postId: String, status: Int): CallResult<Unit>

    suspend fun requestPost(postId: String): CallResult<Unit>

}

class PostRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val mediaRepository: MediaRepository,
) : PostRepository {
    override suspend fun createPost(post: Post, images: List<Uri>): CallResult<Unit> {
        return try {
            val document = firestore.collection(COLLECTION_POST).add(post.toMap()).await()
            val urls = mediaRepository.uploadPostImages(document.id, images)

            val docData = hashMapOf<String, Any>(
                "images" to urls,
                "id" to document.id,
            )
            firestore.collection(COLLECTION_POST)
                .document(document.id)
                .update(docData)
                .await()

            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun getPost(postId: String): CallResult<Post> {
        return try {
            val snapshot = firestore.collection(COLLECTION_POST).document(postId)
                .get()
                .await()
            CallResult.Success(snapshot.toPost())
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun getPost(): CallResult<List<Post>> {
        return try {
            val snapshot = firestore.collection(COLLECTION_POST)
                .whereEqualTo("status", 1)
                .whereNotEqualTo("userId", getCurrentUserId())
                .get()
                .await()
            val list = snapshot.documents.map {
                it.toPost()
            }
            CallResult.Success(list)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun getMyPosts(): CallResult<List<Post>> {
        return try {
            val snapshot = firestore.collection(COLLECTION_POST)
                .whereEqualTo("userId", getCurrentUserId())
                .get()
                .await()
            val list = snapshot.documents.map {
                it.toPost()
            }
            CallResult.Success(list)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun searchPost(query: String): CallResult<List<Post>> {
        return try {
            val keywords = query.lowercase().split("\\s+".toRegex())

            val snapshot = firestore.collection(COLLECTION_POST)
                .whereEqualTo("status", 1)
                .whereArrayContainsAny("keywords", keywords)
                .whereNotEqualTo("userId", getCurrentUserId())
                .get()
                .await()
            val list = snapshot.documents.map {
                it.toPost()
            }
            CallResult.Success(list)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun updatePostStatus(postId: String, status: Int): CallResult<Unit> {
        return try {
            val docData = hashMapOf<String, Any>(
                "status" to status
            )
            firestore.collection(COLLECTION_POST)
                .document(postId)
                .update(docData)
                .await()

            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun requestPost(postId: String): CallResult<Unit> {
        return try {
            val result = getPost(postId)
            if (result is CallResult.Success) {
                val requestedUsers = result.data.requestedUsers.toMutableSet()
                requestedUsers.add(getCurrentUserId())

                val data = hashMapOf<String, Any>(
                    "requestedUsers" to requestedUsers.toList()
                )
                firestore.collection(COLLECTION_POST)
                    .document(postId)
                    .update(data)
                    .await()
            } else if (result is CallResult.Failure) {
                CallResult.Failure(result.message)
            }
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }


    private fun getCurrentUserId() = auth.currentUser?.uid ?: ""
}