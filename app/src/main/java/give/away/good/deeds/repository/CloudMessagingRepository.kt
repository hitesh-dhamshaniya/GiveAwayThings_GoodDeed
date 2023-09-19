package give.away.good.deeds.repository

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.gson.Gson
import give.away.good.deeds.network.model.ChatGroup
import give.away.good.deeds.network.model.PostInfo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayInputStream

private const val SCOPES = "https://www.googleapis.com/auth/firebase.messaging"
private const val FCM_URL =
    "https://fcm.googleapis.com/v1/projects/giveawaythings-gooddeed/messages:send"

const val NOTIFICATION_TOPIC_GENERAL = "general"
const val NOTIFICATION_TOPIC_NEW_POST = "new_post"

const val NOTIFICATION_TYPE_POST = "new_post"
const val NOTIFICATION_TYPE_MESSAGE = "message"
const val NOTIFICATION_TYPE_GENERAL = "general"

/**
 * Repository for message
 * @author Hitesh
 * @since 02.09.2023
 */
interface CloudMessagingRepository {
    fun broadcastNewPost(postInfo: PostInfo)

    suspend fun sendChatMessagePush(chatGroup: ChatGroup)
}

class CloudMessagingRepositoryImpl(
    private val userRepository: UserRepository,
    private val userConfigRepository: UserConfigRepository
) : CloudMessagingRepository {

    override fun broadcastNewPost(postInfo: PostInfo) {
        val post = postInfo.post
        val userName = postInfo.user?.getName()

        val data = mutableMapOf<String, String>(
            "title" to "Give Away Alert!",
            "body" to "$userName posted a give away. Do checkout!",
            "type" to NOTIFICATION_TYPE_POST,
            "data" to Gson().toJson(post),
        )

        post.images.firstOrNull()?.let {
            data["image"] = it
        }

        val params = mapOf<String, Any>(
            "message" to mapOf(
                "topic" to NOTIFICATION_TOPIC_NEW_POST,
                "data" to data
            )
        )
        sendPush(params)
    }

    override suspend fun sendChatMessagePush(chatGroup: ChatGroup) {
        val userInfo = userRepository.getUser() as? CallResult.Success
        val userConfig =
            userConfigRepository.getUserConfig(chatGroup.user?.id ?: "") as? CallResult.Success

        val fcmTokens = userConfig?.data?.fcmToken
        if (fcmTokens.isNullOrEmpty()) {
            Log.w("SendPush", "User has no fcm tokens")
            return
        }

        val data = mutableMapOf(
            "title" to "New Message from ${userInfo?.data?.getName()}!",
            "body" to (chatGroup.chat?.message ?: ""),
            "type" to NOTIFICATION_TYPE_MESSAGE,
            "data" to chatGroup.id,
        )

        val profilePic = chatGroup.user?.profilePic
        if (!profilePic.isNullOrBlank()) {
            data["icon"] = profilePic
        }

        fcmTokens.forEach { token ->
            val params = mapOf<String, Any>(
                "message" to mapOf(
                    "token" to token,
                    "data" to data
                )
            )
            sendPush(params)
        }
    }

    private fun sendPush(params: Map<String, Any>) {
        val json = Gson().toJson(params)
        Log.i("FCM Push", "request.body: $json")
        val accessToken = getAccessToken() ?: return

        val request = Request.Builder()
            .url(FCM_URL)
            .post(Gson().toJson(params).toRequestBody("application/json".toMediaType()))
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = OkHttpClient().newCall(request).execute()
        Log.i("FCM Push", "response.code: ${response.code}")
        Log.i("FCM Push", "response.message ${response.message}")
        Log.i("FCM Push", "response.body ${response.body?.string()}")
    }

    private fun getAccessToken(): String? {
        return try {
            val inputStream = ByteArrayInputStream(credentials.toByteArray())
            val credential: GoogleCredentials = GoogleCredentials
                .fromStream(inputStream)
                .createScoped(SCOPES)
            credential.accessToken?.tokenValue ?: credential.refreshAccessToken().tokenValue
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    private val credentials = """
        {
          "type": "service_account",
          "project_id": "giveawaythings-gooddeed",
          "private_key_id": "c8996bc2d310a7728b272b6c3a441251d77b449b",
          "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCvsFvEDXIpvegJ\n6zUal+o1mWodKiXcC/TOFR3cRUphAWc57GOb63UywGSch8Q0ynQM1sjit8SOR8RC\n2Eq2wyXqRN/Vr07UFq57o253wsnHvI6GvYzHqzwXddW73Tt47yNSYcNGptISSClD\nMbH8Z3IJXnTDotHWLuLo5eenrV/pkP/EkxE0gODImNfx1ahcJ4cgGc0NtTSoaJPV\n8RI5mJVSVh+fFiLXv95JURe2ezHXU/xJiQ8xGAFaamofa91hv4ie2lGff9XESwwH\nUN0NHBDgLdG/zf2R7ioJpyzW8LMxBAUNK55KzwQJEZ1WNNQBFdfgGQ/7wsU34TUb\n0jG7Jh3XAgMBAAECggEAEqL6mQl0GslyW9r/fxquVZE4mG9oJLd/+Hx09nDDOxcT\nBCn3RwrrG78c7R3Zyl/Pf60pEiO8Tft+7jhTdJ+u3fMAa4J1MjpVt/fWOA4JqY5u\n8AwbavLXJQXy7kLBlEqJzKtiM5RupFHTTrVu0/+FMUk7yaTz7Wx71YjN0hOqIPR7\nV1RhAAjxd7IbVQxZgPseoLqbaBKBTQ0Ez9ULdXWwFUpocPIF6o5rzIiHh8rDokDv\nFM6oZ8b6PWq66ssK1wLOrWymilWDtJWAxArBPNzqvQPflTXLLlP4PkN00wOu/Wh5\naAHfdTg5ehxwHH2FdwSLCrClPgpQ3/J8/FrhGuWzgQKBgQDv4D1FD6hnF3Zh/5Wl\noWCHxBbIqXEivN9ZC+xgJMTwfW1LSp/wWCW3Krc/YnBOpneIYRdl8x50z7NNBX7S\ndQRwlGGWvNu/Hfr5DLYa0zf+MzI+qrG3f2adk/nfnEAJuALP0eY0vMQ+FAVp84V0\nuQOFRgTkeSXShZ74QApLpe3lKQKBgQC7f5hc65+QWhk3hiWM/cYgzRblp83jZBX5\nzOVZBcKl5n5FVF73aVkb0PbJStNgy/FR0YySUlMH0Fm3Q4naPdSDjupL3JmOLTDx\n7YNKotVbqcsXm9XaGaGGfFD7GDRFRXiPp0P1hOvjn/zM1V5Jm/TJpSMyVY6NkW9s\nVJF+LIlK/wKBgQDd9K6c1YUshxA6U4VDshQg6/dtCyQtUXiU66x3te4o3NYhodZm\nTRnw52ySEOL8U5otLjG5WwFmcr1k2l3kHPTTuuil0vC+Yd5VTka+gYzBANxQEh/J\nO/BWhJLkLq8ujv7DD2SkjbpD+sCbrgJh48P3MLfcNKEjsBJNgOxF3S5IKQKBgD4y\neWx5jRjpimvsyBAZLtw6rFnKbHX+AXw4B+ycCJHYABMnhck03SZpDzR+6de2T3sA\ntEzlmj2BVAGDGfuXfyOVCQvZRebv/siCLYdYtMYPcRuHykzrK7XdD0/8G3rPLMsa\nhWSwjz+XFtkr/ncUkQNDEIXl87/qerfI5eqhA3opAoGBALkbOIrYbwZlRJH/r2eH\ndZk3p4ATinDnj5oa6PsocPkD6Oqxpwl8+AYFaU6zCbqQ2NjtNtWI0Jbm72PAG0J9\nJhm47bfTV7Q3f5yfEfA7tPDWoFba5KN0TyK6RnbUSZKvSxY14H5hD7Gd7Wvnw0hb\nROWIHP6edwU6plJpPhwycM2/\n-----END PRIVATE KEY-----\n",
          "client_email": "firebase-adminsdk-5qxqq@giveawaythings-gooddeed.iam.gserviceaccount.com",
          "client_id": "106157372217425511751",
          "auth_uri": "https://accounts.google.com/o/oauth2/auth",
          "token_uri": "https://oauth2.googleapis.com/token",
          "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
          "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-5qxqq%40giveawaythings-gooddeed.iam.gserviceaccount.com",
          "universe_domain": "googleapis.com"
        }
    """.trimIndent()

}