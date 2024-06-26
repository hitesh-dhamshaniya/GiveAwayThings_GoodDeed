package give.away.good.deeds.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import give.away.good.deeds.network.model.User
import kotlinx.coroutines.tasks.await

private const val COLLECTION_USER: String = "users"

interface UserRepository {

    suspend fun getUser(): CallResult<User>
    suspend fun getUserFromCache(userId: String): User?

    suspend fun createUser(
        userId: String, firstName: String, lastName: String, email: String
    ): CallResult<Unit>

    suspend fun updateUser(
        userId: String, data: Map<String, Any>
    ): CallResult<Unit>
}

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUser(): CallResult<User> {
        return try {
            firebaseAuth.currentUser?.let {
                val document = firestore.collection(COLLECTION_USER).document(it.uid)
                val snapshot = document.get().await()
                snapshot.toObject(User::class.java)
            }?.let { user ->
                CallResult.Success(user)
            } ?: CallResult.Failure("")
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun getUserFromCache(userId: String): User? {
        var user = UserCache.get(userId)
        if (user == null) {
            user = getUser(userId)
            UserCache.add(user)
        }
        return user
    }

    override suspend fun createUser(
        userId: String, firstName: String, lastName: String, email: String
    ): CallResult<Unit> {
        val user = User(userId, firstName, lastName, email)
        return try {
            val document = firestore.collection(COLLECTION_USER).document(userId)
            document.set(user).await()
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun updateUser(
        userId: String, data: Map<String, Any>
    ): CallResult<Unit> {
        return try {
            firestore.collection(COLLECTION_USER).document(userId).update(data).await()
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    private suspend fun getUser(userId: String): User? {
        val document = firestore.collection(COLLECTION_USER).document(userId)
        val snapshot = document.get().await()
        return snapshot.toObject(User::class.java)
    }

}