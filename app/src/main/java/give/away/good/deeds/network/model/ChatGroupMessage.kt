package give.away.good.deeds.network.model

data class ChatGroupMessage(
    val groupId: String,
    val me: User?,
    val user: User?,
    val messageList: List<ChatMessage>
)
