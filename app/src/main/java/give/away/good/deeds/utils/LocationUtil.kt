package give.away.good.deeds.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.util.Locale


object LocationUtil {

    fun distanceBetween(point1: LatLng?, point2: LatLng?): Float {
        return if (point1 == null || point2 == null) {
            0f
        } else {
            SphericalUtil.computeDistanceBetween(point1, point2).toFloat()
        }
    }

    @Suppress("DEPRECATION")
    fun getAddress(context: Context, latLng: LatLng): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: Address?

        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0]
                address.getAddressLine(0)
            } else {
                null
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

}