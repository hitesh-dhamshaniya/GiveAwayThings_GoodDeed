package give.away.good.deeds.network.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FieldValue
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Notification(
    val title: String,
    val message: String,
    val time: Date,
    val type: String,
    val userId: String? = null,
    val icon: String? = null,
    val image: String? = null,
    val data: String? = null,
) : Parcelable {


    @Exclude
    fun toMap(userId: String) = mapOf<String, Any?>(
        "userId" to userId,
        "title" to title,
        "message" to message,
        "type" to type,
        "image" to image,
        "icon" to icon,
        "data" to data,
        "timestamp" to FieldValue.serverTimestamp(),
    )

}

@Suppress("UNCHECKED_CAST")
fun DocumentSnapshot.toNotification(): Notification {
    return Notification(
        userId = getString("userId") ?: "",
        title = getString("title") ?: "",
        message = getString("message") ?: "",
        type = getString("type") ?: "",
        image = getString("image") ?: "",
        icon = getString("icon") ?: "",
        data = getString("data") ?: "",
        time = getTimestamp("timestamp")?.toDate() ?: Date(),
    )
}
