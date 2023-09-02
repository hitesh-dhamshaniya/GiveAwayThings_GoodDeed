package give.away.good.deeds.ui.screens.main.messages.detail


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import give.away.good.deeds.network.model.ChatMessage
import give.away.good.deeds.utils.TimeAgo


@Composable
fun ChatCard(
    chatMessage: ChatMessage,
    userId: String,
) {
    val isMyMessage = userId == chatMessage.senderId
    val modifier =
        if (!isMyMessage) Modifier.padding(end = 64.dp) else Modifier.padding(start = 64.dp)
    val shape =
        if (isMyMessage) RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp) else RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)
    Card(
        modifier = modifier,
        shape = shape
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                chatMessage.message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = TimeAgo.timeAgo(chatMessage.timestamp.time),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}