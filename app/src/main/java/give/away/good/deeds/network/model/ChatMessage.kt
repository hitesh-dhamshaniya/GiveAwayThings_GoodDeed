package give.away.good.deeds.network.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FieldValue
import java.util.Date

data class ChatMessage(
    val id: String,
    val senderId: String,
    val message: String,
    val timestamp: Date = Date()
) {

    @Exclude
    fun toMap() = mapOf<String, Any?>(
        "senderId" to senderId,
        "message" to message,
        "timestamp" to FieldValue.serverTimestamp(),
    )

}

fun DocumentSnapshot.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        senderId = getString("senderId") ?: "",
        message = getString("message") ?: "",
        timestamp = getTimestamp("timestamp")?.toDate() ?: Date(),
    )
}
