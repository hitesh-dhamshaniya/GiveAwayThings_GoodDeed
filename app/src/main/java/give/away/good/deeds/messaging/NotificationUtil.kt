package give.away.good.deeds.messaging

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.permissionx.guolindev.PermissionX
import give.away.good.deeds.repository.NOTIFICATION_TOPIC_GENERAL
import give.away.good.deeds.repository.NOTIFICATION_TOPIC_NEW_POST

fun FragmentActivity.requestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionX.init(this)
            .permissions(Manifest.permission.POST_NOTIFICATIONS)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Notification permission are required to receive updates",
                    "OK",
                    "Cancel"
                )
            }.request { _, _, _ ->
                NotificationManager(this).createChannel()
            }
    } else {
        NotificationManager(this).createChannel()
    }
}


fun subscribeToTopic() {
    val firebaseMessaging = FirebaseMessaging.getInstance()
    firebaseMessaging.subscribeToTopic(NOTIFICATION_TOPIC_GENERAL)
    firebaseMessaging.subscribeToTopic(NOTIFICATION_TOPIC_NEW_POST)
}

fun unsubscribeFromTopic() {
    val firebaseMessaging = FirebaseMessaging.getInstance()
    firebaseMessaging.unsubscribeFromTopic(NOTIFICATION_TOPIC_GENERAL)
    firebaseMessaging.unsubscribeFromTopic(NOTIFICATION_TOPIC_NEW_POST)
}