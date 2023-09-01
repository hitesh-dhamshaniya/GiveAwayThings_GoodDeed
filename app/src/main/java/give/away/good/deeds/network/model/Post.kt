package give.away.good.deeds.network.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class Post(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val quantity: Int = 1,
    val location: LatLng? = null,

    val createdDateTime: Date = Date(),
    val pickupDateTime: Date? = null,

    val status: Int = 1,

    // list of users
    val requestedUsers: List<String> = emptyList(),
) {

    @Exclude
    fun toMap() = mapOf<String, Any?>(
        "userId" to userId,
        "title" to title,
        "description" to description,
        //"images" to images,
        "quantity" to quantity,
        "location" to location?.let { GeoPoint(it.latitude, it.longitude) },

        "quantity" to quantity,
        "createdDateTime" to FieldValue.serverTimestamp(),
    )

    fun isActive() = status == 1

    fun isClosed() = status == 0

    fun isCancelled() = status == -1

}

fun DocumentSnapshot.toPost(): Post {
    return Post(
        id = id,
        title = getString("title") ?: "",
        description = getString("description") ?: "",
        quantity = getLong("quantity")?.toInt() ?: 1,
        status = getLong("status")?.toInt() ?: 1,

        location = get("location", GeoPoint::class.java)?.let {
            LatLng(it.latitude, it.longitude)
        },

        images = get("images") as? List<String> ?: emptyList(),
        requestedUsers = get("requestedUsers") as? List<String> ?: emptyList(),

        createdDateTime = getTimestamp("createdDateTime")?.toDate() ?: Date(),
        pickupDateTime = getTimestamp("pickupDateTime")?.toDate(),
    )
}

/*
var totalView: Int = 0,
var likes: Int = 0,
var giveAwayUserName: String? = "",
var isItFood: Boolean = false,
*/
