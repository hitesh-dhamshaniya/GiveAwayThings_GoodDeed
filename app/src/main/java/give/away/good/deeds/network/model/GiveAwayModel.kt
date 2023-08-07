package give.away.good.deeds.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class GiveAwayModel(
    var id: String? = "",
    var itemImageUrl: String? = "",
    var title: String? = "",
    var userProfilePicUrl: String? = "",
    var distance: Double? = null,
    var totalView: Int = 0,
    var postUserId: String? = "",
    var likes: Int = 0,
    var pickUpDateTime: String? = "",
    var postAddedTime: Timestamp? = null,
    var postLatitude: Double? = null,
    var postLongitude: Double? = null,
    var giveAwayUserName: String? = "",
    var quantity: Int? = 0,
    var isItFood: Boolean = false,
    val pickUpRequestUserIds: String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "itemImageURL" to itemImageUrl,
            "title" to title,
            "userProfilePicUrl" to userProfilePicUrl,
            "distance" to distance,
            "totalView" to totalView,
            "postUserId" to postUserId,
            "likes" to likes,
            "postAddedTime" to postAddedTime,
            "pickUpDateTime" to pickUpDateTime,
            "postLatitude" to postLatitude,
            "postLongitude" to postLongitude,
            "giveAwayUserName" to giveAwayUserName,
            "quantity" to quantity,
            "isItFood" to isItFood,
            "pickUpRequestUserIds" to pickUpRequestUserIds
        )
    }
}