package give.away.good.deeds.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import give.away.good.deeds.network.model.UserConfig
import give.away.good.deeds.network.model.toUserConfig
import kotlinx.coroutines.tasks.await

private const val COLLECTION_USER_CONFIG: String = "user-config"

interface UserConfigRepository {

    suspend fun getUserConfig(): CallResult<UserConfig>

    suspend fun getUserConfig(userId: String): CallResult<UserConfig>

    suspend fun setLocation(userConfig: UserConfig): CallResult<Unit>

    suspend fun addToken(token: String): CallResult<Unit>

}

class UserConfigRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : UserConfigRepository {

    override suspend fun getUserConfig(): CallResult<UserConfig> {
        val userId = getUserId()
        return getUserConfig(userId)
    }

    override suspend fun getUserConfig(userId: String): CallResult<UserConfig> {
        return try {
            val document = firestore.collection(COLLECTION_USER_CONFIG).document(userId)
            val snapshot = document.get().await()
            snapshot.toUserConfig().let { config ->
                CallResult.Success(config)
            }
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun setLocation(userConfig: UserConfig): CallResult<Unit> {
        return try {
            val docData = mapOf<String, Any>(
                "location" to GeoPoint(userConfig.latLng!!.latitude, userConfig.latLng.longitude),
                "address" to userConfig.address,
            )

            val userId = getUserId()
            val document = firestore.collection(COLLECTION_USER_CONFIG).document(userId)

            val snapshot = document.get().await()
            val reference = if (snapshot.exists()) {
                document.update(docData)
            } else {
                document.set(docData)
            }

            reference.await()
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun addToken(token: String): CallResult<Unit> {
        return try {
            val result = getUserConfig()
            val userId = getUserId()
            if (result is CallResult.Success) {
                val list = result.data.fcmToken.toMutableList()
                list.add(token)

                val data = mapOf<String, Any>(
                    "fcmToken" to list.toSet().toList(),
                )

                firestore.collection(COLLECTION_USER_CONFIG)
                    .document(userId)
                    .update(data)
                    .await()
            } else {
                val data = mapOf<String, Any>(
                    "fcmToken" to listOf<String>(token),
                )

                firestore.collection(COLLECTION_USER_CONFIG)
                    .document(userId)
                    .set(data)
                    .await()
            }

            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    private fun getUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

}