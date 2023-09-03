package give.away.good.deeds.network.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot

data class UserConfig(
    val latLng: LatLng?,
    val address: String
)


fun DocumentSnapshot.toUserConfig(): UserConfig? {
    val geoPoint = getGeoPoint("location") ?: return null
    return UserConfig(
        latLng = LatLng(geoPoint.latitude, geoPoint.longitude),
        address = getString("address") ?: ""
    )
}