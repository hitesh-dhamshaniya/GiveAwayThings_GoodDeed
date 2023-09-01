package give.away.good.deeds.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil


object LocationUtil {

    fun distanceBetween(point1: LatLng?, point2: LatLng?): Float {
        return if (point1 == null || point2 == null) {
            0f
        } else {
            SphericalUtil.computeDistanceBetween(point1, point2).toFloat()
        }
    }

}