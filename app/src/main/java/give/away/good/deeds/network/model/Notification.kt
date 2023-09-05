package give.away.good.deeds.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    var id: Long = 0,
    val title: String,
    val message: String,
    val time: Long,
    val type: String
) : Parcelable
