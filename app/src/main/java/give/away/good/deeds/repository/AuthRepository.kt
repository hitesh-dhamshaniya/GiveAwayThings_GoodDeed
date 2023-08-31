package give.away.good.deeds.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun forgotPassword(email: String): CallResult<Unit>

    suspend fun login(email: String, password: String): CallResult<Unit>

    suspend fun register(email: String, password: String): CallResult<String>

    suspend fun changePassword(currentPassword: String, newPassword: String): CallResult<Unit>

    suspend fun signOut()
}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun forgotPassword(email: String): CallResult<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun login(email: String, password: String): CallResult<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun register(email: String, password: String): CallResult<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            CallResult.Success(result.user?.uid ?: "")
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): CallResult<Unit> {
        return try {
            val email = firebaseAuth.currentUser?.email ?: ""
            when (val loginError = login(email, currentPassword)) {
                is CallResult.Success -> {
                    firebaseAuth.currentUser?.updatePassword(newPassword)
                    CallResult.Success(Unit)
                }

                is CallResult.Failure -> {
                    CallResult.Failure(loginError.message)
                }
            }
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

}