package give.away.good.deeds.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import give.away.good.deeds.network.model.UserConfig
import give.away.good.deeds.network.model.toUserConfig
import kotlinx.coroutines.tasks.await

private const val COLLECTION_USER_CONFIG: String = "user-config"

interface UserConfigRepository {

    suspend fun getLocation(): CallResult<UserConfig>
    suspend fun setLocation(userConfig: UserConfig): CallResult<Unit>

}

class UserConfigRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserConfigRepository {
    override suspend fun getLocation(): CallResult<UserConfig> {
        return try {
            firebaseAuth.currentUser?.let {
                val document = firestore.collection(COLLECTION_USER_CONFIG).document(it.uid)
                val snapshot = document.get().await()
                snapshot.toUserConfig()
            }?.let { config ->
                CallResult.Success(config)
            } ?: CallResult.Failure("")
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun setLocation(userConfig: UserConfig): CallResult<Unit> {
        return try {
            val docData = hashMapOf(
                "location" to GeoPoint(userConfig.latLng!!.latitude, userConfig.latLng.longitude),
                "address" to userConfig.address,
            )

            firebaseAuth.currentUser?.let {
                firestore.collection(COLLECTION_USER_CONFIG)
                    .document(it.uid)
                    .set(docData)
                    .await()
                CallResult.Success(Unit)
            } ?: CallResult.Failure("")
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

}