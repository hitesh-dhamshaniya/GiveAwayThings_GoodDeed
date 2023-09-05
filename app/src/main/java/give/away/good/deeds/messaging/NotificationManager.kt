package give.away.good.deeds.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Notification
import give.away.good.deeds.ui.screens.main.MainActivity

private const val CHANNEL_ID = "fcm_default_push"

class NotificationManager(context: Context) : ContextWrapper(context) {

    private val notificationManager = getNotificationManager(context)

    fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Push Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Show push notification"
        notificationManager.createNotificationChannel(channel)
    }

    fun showPush(notification: Notification, bitmap: Bitmap?, icon: Bitmap?) {
        val builder = getNotificationBuilder(notification.title, notification.message)
        builder.setLargeIcon(icon)
        builder.setContentIntent(getPendingIntent(getNotificationIntent(notification)))

        if (bitmap != null) {
            val bigPicStyle = NotificationCompat.BigPictureStyle()
            bigPicStyle.setBigContentTitle(notification.title)
            bigPicStyle.setSummaryText(notification.message)
            bigPicStyle.bigPicture(bitmap)
            builder.setStyle(bigPicStyle)
        }

        val notificationId = getUniqueNotificationId()
        notificationManager.notify(notificationId, builder.build())
    }

    private fun getNotificationIntent(notification: Notification): Intent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("data", notification)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

    private fun getDefaultContentIntent(): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return getPendingIntent(intent)
    }

    private fun getPendingIntent(intent: Intent): PendingIntent {
        val reqCode = getUniqueNotificationId()
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(this, reqCode, intent, getFlag())
    }

    private fun getUniqueNotificationId(): Int {
        return (System.nanoTime() % Integer.MAX_VALUE).toInt() % 1000000
    }

    private fun getNotificationBuilder(
        title: String,
        message: String
    ): NotificationCompat.Builder {
        return NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setAutoCancel(true)
            .setColorized(true)
            .setShowWhen(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(getDefaultContentIntent())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getFlag(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_ONE_SHOT
    }


}