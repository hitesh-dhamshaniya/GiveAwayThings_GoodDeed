package give.away.good.deeds.network.model

data class ChatGroup(
    val id: String,
    val user: User?,
    val chat: ChatMessage?
)
