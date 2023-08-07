package give.away.good.deeds.network.model

data class MyGiveAwayModel(
    val id: String,
    val title: String,
    val date: String,
    val imageUrl: String,
    val receivedRequests: String
) {
}