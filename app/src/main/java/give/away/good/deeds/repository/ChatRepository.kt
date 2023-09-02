package give.away.good.deeds.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import give.away.good.deeds.network.model.ChatGroup
import give.away.good.deeds.network.model.ChatMessage
import give.away.good.deeds.network.model.toChatMessage
import give.away.good.deeds.network.model.toPost
import kotlinx.coroutines.tasks.await

private const val COLLECTION_CHAT_GROUP: String = "chat-group"
private const val COLLECTION_CHAT_MESSAGES: String = "chat-messages"

interface ChatRepository {

    suspend fun createChatGroup(postUserId: String): CallResult<String>

    suspend fun sendMessage(groupId: String, chatMessage: ChatMessage): CallResult<Unit>

    suspend fun getMyChatGroups(): CallResult<List<ChatGroup>>

}

class ChatRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : ChatRepository {

    override suspend fun createChatGroup(postUserId: String): CallResult<String> {
        return try {
            val currentUserId = getCurrentUserId()

            val participants = listOf(postUserId, currentUserId)
            val snapshot = firestore.collection(COLLECTION_CHAT_GROUP)
                .whereArrayContains("participants", participants)
                .limit(1)
                .get().await()

            if (snapshot.isEmpty) {
                val data = hashMapOf<String, Any>(
                    "participants" to participants,
                    "status" to 1,
                )
                val documentId = firestore.collection(COLLECTION_CHAT_GROUP).add(data).await().id
                CallResult.Success(documentId)
            } else {
                CallResult.Success(snapshot.first().id)
            }

        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun sendMessage(groupId: String, chatMessage: ChatMessage): CallResult<Unit> {
        return try {
            firestore.collection(COLLECTION_CHAT_GROUP)
                .document(groupId)
                .collection(COLLECTION_CHAT_MESSAGES).add(chatMessage.toMap())
            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun getMyChatGroups(): CallResult<List<ChatGroup>> {
        return try {
            val userId = getCurrentUserId() ?: ""
            val snapshot = firestore.collection(COLLECTION_CHAT_GROUP)
                .whereArrayContains("participants", userId)
                .get()
                .await()

            val list = snapshot.documents.map { document ->
                val participants = document.get("participants") as? List<String> ?: emptyList()
                val participants1 = participants.toMutableList()
                participants1.remove(userId)
                val participantId = participants1.first()

                val message = firestore.collection(COLLECTION_CHAT_GROUP)
                    .document(document.id)
                    .collection(COLLECTION_CHAT_MESSAGES)
                    .limit(1)
                    .get()
                    .await()
                    .firstOrNull()

                ChatGroup(
                    id = document.id,
                    chat = message?.toChatMessage(),
                    user = userRepository.getUserFromCache(participantId)
                )
            }

            CallResult.Success(list)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    private fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}