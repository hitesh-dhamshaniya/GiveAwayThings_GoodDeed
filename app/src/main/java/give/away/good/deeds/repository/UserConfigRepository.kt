package give.away.good.deeds.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await

private const val COLLECTION_USER_CONFIG: String = "user-config"

interface UserConfigRepository {

    suspend fun getLocation(): CallResult<LatLng>
    suspend fun setLocation(latLng: LatLng): CallResult<Unit>

}

class UserConfigRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserConfigRepository {
    override suspend fun getLocation(): CallResult<LatLng> {
        return try {
            firebaseAuth.currentUser?.let {
                val document = firestore.collection(COLLECTION_USER_CONFIG).document(it.uid)
                val snapshot = document.get().await()
                snapshot.getGeoPoint("location")?.let {  point ->
                    LatLng(point.latitude, point.longitude)
                }
            }?.let { latLng ->
                CallResult.Success(latLng)
            } ?: CallResult.Failure("")
        } catch (ex: FirebaseFirestoreException) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun setLocation(latLng: LatLng): CallResult<Unit> {
        return try {
            val docData = hashMapOf(
                "location" to GeoPoint(latLng.latitude, latLng.longitude),
            )

            firebaseAuth.currentUser?.let {
                firestore.collection(COLLECTION_USER_CONFIG)
                    .document(it.uid)
                    .set(docData)
                    .await()
                CallResult.Success(Unit)
            } ?: CallResult.Failure("")
        } catch (ex: FirebaseFirestoreException) {
            CallResult.Failure(ex.message)
        }
    }

}