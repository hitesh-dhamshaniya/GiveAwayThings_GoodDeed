package give.away.good.deeds.messaging

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Notification
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.AuthRepositoryImpl
import give.away.good.deeds.repository.NOTIFICATION_TYPE_GENERAL
import give.away.good.deeds.repository.NOTIFICATION_TYPE_MESSAGE
import give.away.good.deeds.repository.NOTIFICATION_TYPE_POST
import java.net.HttpURLConnection
import java.net.URL

class MessageWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val repository = AuthRepositoryImpl(firebaseAuth = FirebaseAuth.getInstance())
        if (!repository.isLoggedIn()) {
            return Result.success()
        }

        val title = inputData.getString("title") ?: applicationContext.getString(R.string.app_name)
        val body = inputData.getString("body")
        val type = inputData.getString("type") ?: NOTIFICATION_TYPE_GENERAL
        val bitmap = inputData.getString("image")?.let { url ->
            getBitmapFromUrl(url)
        }
        val icon = inputData.getString("icon")?.let { url ->
            getBitmapFromUrl(url)
        }
        val data = inputData.getString("data")

        if (body.isNullOrBlank()) {
            Log.e(TAG, "Missing message body")
            return Result.success()
        }

        val notification = Notification(
            title = title,
            message = body,
            type = type,
            time = System.currentTimeMillis()
        )

        when (type) {
            NOTIFICATION_TYPE_POST -> {
                handlePost(data, notification, bitmap, icon)
            }

            NOTIFICATION_TYPE_MESSAGE -> {
                val manager = NotificationManager(applicationContext)
                manager.showPush(notification, bitmap, icon)
            }

            else -> {
                val manager = NotificationManager(applicationContext)
                manager.showPush(notification, bitmap, icon)
            }
        }

        return Result.success()
    }

    private fun handlePost(
        data: String?,
        notification: Notification,
        bitmap: Bitmap?,
        icon: Bitmap?
    ) {
        if (data.isNullOrBlank()) return;
        val post = Gson().fromJson(data, Post::class.java) ?: return

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (post.userId != uid) {
            val manager = NotificationManager(applicationContext)
            manager.showPush(notification, bitmap, icon)
        } else {
            Log.w(TAG, "Skip post as you're the owner")
        }
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return try {
            val connection = URL(imageUrl).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            BitmapFactory.decodeStream(connection.inputStream).roundCorner()
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    private fun Bitmap.roundCorner(): Bitmap {
        val output: Bitmap = Bitmap.createBitmap(
            width, height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        val rectF = RectF(rect)
        val roundPx = width / 2.0f
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, rect, rect, paint)
        return output
    }

    companion object {
        private const val TAG = "MessageWorker"
    }
}