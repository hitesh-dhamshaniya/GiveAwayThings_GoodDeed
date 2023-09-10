package give.away.good.deeds.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import give.away.good.deeds.network.model.Notification
import give.away.good.deeds.network.model.toNotification
import kotlinx.coroutines.tasks.await

const val COLLECTION_NOTIFICATION: String = "notification"

interface NotificationRepository {

    suspend fun add(notification: Notification) : CallResult<Unit>

    suspend fun getNotifications() : CallResult<List<Notification>>

}

class NotificationRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : NotificationRepository {
    override suspend fun add(notification: Notification): CallResult<Unit> {
        return try {
            val userId = getCurrentUserId()
            firestore.collection(COLLECTION_NOTIFICATION).add(notification.toMap(userId))
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun getNotifications(): CallResult<List<Notification>> {
        return try {
            val snapshot = firestore.collection(COLLECTION_NOTIFICATION)
                .whereEqualTo("userId", getCurrentUserId())
                .get()
                .await()
            val list = snapshot.documents.map {
               it.toNotification()
            }
            CallResult.Success(list)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    private fun getCurrentUserId() = auth.currentUser?.uid ?: ""

}