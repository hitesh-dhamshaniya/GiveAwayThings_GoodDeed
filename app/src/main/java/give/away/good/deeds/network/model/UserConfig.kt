package give.away.good.deeds.network.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot

data class UserConfig(
    val latLng: LatLng?,
    val address: String,
    val fcmToken: List<String> = emptyList()
)


fun DocumentSnapshot.toUserConfig(): UserConfig {
    return UserConfig(
        latLng = getGeoPoint("location")?.let { geoPoint ->
            LatLng(geoPoint.latitude, geoPoint.longitude)
        },
        address = getString("address") ?: "",
        fcmToken = get("fcmToken") as? List<String> ?: emptyList(),
    )
}