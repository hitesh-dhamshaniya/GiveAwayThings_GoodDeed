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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Notification
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.AuthRepositoryImpl
import give.away.good.deeds.repository.NOTIFICATION_TYPE_GENERAL
import give.away.good.deeds.repository.NOTIFICATION_TYPE_MESSAGE
import give.away.good.deeds.repository.NOTIFICATION_TYPE_POST
import give.away.good.deeds.repository.NotificationRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date

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

        val image = inputData.getString("image")
        val imageBitmap = image?.let { url ->
            getBitmapFromUrl(url)
        }

        val icon = inputData.getString("icon")
        val iconBitmap = icon?.let { url ->
            getBitmapFromUrl(url, isRounded = true)
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
            time = Date(),

            icon = icon,
            image = image,
            data = data,
        )

        when (type) {
            NOTIFICATION_TYPE_POST -> {
                handlePost(data, notification, imageBitmap, iconBitmap)
            }

            NOTIFICATION_TYPE_MESSAGE -> {
                showPush(notification, imageBitmap, iconBitmap)
            }

            else -> {
                showPush(notification, imageBitmap, iconBitmap)
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
            showPush(notification, bitmap, icon)
        } else {
            Log.w(TAG, "Skip post as you're the owner")
        }
    }

    private fun getBitmapFromUrl(imageUrl: String, isRounded: Boolean = false): Bitmap? {
        return try {
            val connection = URL(imageUrl).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val bitmap = BitmapFactory.decodeStream(connection.inputStream)
            if (isRounded) {
                bitmap.roundCorner()
            } else {
                bitmap
            }
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

    private fun showPush(notification: Notification, image: Bitmap?, icon: Bitmap?){
        val manager = NotificationManager(applicationContext)
        manager.showPush(notification, image, icon)

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        CoroutineScope(Dispatchers.IO).launch {
            NotificationRepositoryImpl(auth, firestore).add(notification)
        }
    }
}