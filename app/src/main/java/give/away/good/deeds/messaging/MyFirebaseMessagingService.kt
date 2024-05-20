package give.away.good.deeds.messaging

import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data);
            scheduleJob(remoteMessage.data)
        }
    }

    private fun scheduleJob(payload: Map<String, Any>) {
        val data = Data.Builder().putAll(payload).build()
        val work = OneTimeWorkRequest.Builder(MessageWorker::class.java)
            .setInputData(data)
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM", "Token:$token")
    }

    companion object {
        private const val TAG = "FCM"
    }
}