package give.away.good.deeds.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import give.away.good.deeds.network.model.ChatGroup
import give.away.good.deeds.network.model.ChatGroupMessage
import give.away.good.deeds.network.model.ChatMessage
import give.away.good.deeds.network.model.toChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val COLLECTION_CHAT_GROUP: String = "chat-group"
 const val COLLECTION_CHAT_MESSAGES: String = "chat-messages"

interface ChatRepository {

    suspend fun createChatGroup(postUserId: String): CallResult<String>

    suspend fun sendMessage(groupId: String, chatMessage: ChatMessage): CallResult<Unit>

    suspend fun getMyChatGroups(): CallResult<List<ChatGroup>>

    suspend fun getChatMessages(groupId: String): CallResult<ChatGroupMessage>

}

@Suppress("UNCHECKED_CAST")
class ChatRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val cloudMessagingRepository: CloudMessagingRepository
) : ChatRepository {

    override suspend fun createChatGroup(postUserId: String): CallResult<String> {
        return try {
            val currentUserId = getCurrentUserId()
            val snapshot = firestore.collection(COLLECTION_CHAT_GROUP)
                .whereEqualTo("participants.$postUserId", true)
                .whereEqualTo("participants.$currentUserId", true)
                .limit(1)
                .get().await()

            if (snapshot.isEmpty) {
                val participantMap = mapOf(postUserId to true, currentUserId to true)
                val data = hashMapOf<String, Any>(
                    "participants" to participantMap,
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

            sendMessagePush(groupId, chatMessage)

            CallResult.Success(Unit)
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }

    override suspend fun getMyChatGroups(): CallResult<List<ChatGroup>> {
        return try {
            val userId = getCurrentUserId() ?: ""
            val snapshot = firestore.collection(COLLECTION_CHAT_GROUP)
                .whereEqualTo("participants.$userId", true)
                .get()
                .await()

            val list = snapshot.documents.map { document ->
                val participantMap = document.get("participants") as? Map<String, Boolean> ?: emptyMap()
                val participants = participantMap.keys.toMutableList()
                val participants1 = participants.toMutableList()
                participants1.remove(userId)
                val participantId = participants1.first()

                val message = firestore.collection(COLLECTION_CHAT_GROUP)
                    .document(document.id)
                    .collection(COLLECTION_CHAT_MESSAGES)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
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

    private suspend fun sendMessagePush(groupId: String, chatMessage: ChatMessage) {
        return withContext(Dispatchers.IO) {
            val userId = getCurrentUserId() ?: ""
            val snapshot = firestore.collection(COLLECTION_CHAT_GROUP)
                .document(groupId)
                .get()
                .await()

            val participantMap = snapshot.get("participants") as? Map<String, Boolean> ?: emptyMap()
            val participants1 = participantMap.keys.toMutableList()
            participants1.remove(userId)
            val participantId = participants1.first()
            val group = ChatGroup(
                id = groupId,
                chat = chatMessage,
                user = userRepository.getUserFromCache(participantId)
            )
            cloudMessagingRepository.sendChatMessagePush(group)
        }
    }

    override suspend fun getChatMessages(groupId: String): CallResult<ChatGroupMessage> {
        return try {
            val userId = getCurrentUserId() ?: ""
            val snapshot = firestore.collection(COLLECTION_CHAT_GROUP)
                .document(groupId)
                .get()
                .await()

            val participantMap = snapshot.get("participants") as? Map<String, Boolean> ?: emptyMap()
            val participants1 = participantMap.keys.toMutableList()
            participants1.remove(userId)
            val participantId = participants1.first()

            val messageList = firestore.collection(COLLECTION_CHAT_GROUP)
                .document(groupId)
                .collection(COLLECTION_CHAT_MESSAGES)
                .get()
                .await()


            CallResult.Success(ChatGroupMessage(
                groupId = groupId,
                me = userRepository.getUserFromCache(getCurrentUserId() ?: ""),
                user = userRepository.getUserFromCache(participantId),
                messageList = messageList.map { it.toChatMessage() }
            ))
        } catch (ex: Exception) {
            CallResult.Failure(ex.message)
        }
    }


    private fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}