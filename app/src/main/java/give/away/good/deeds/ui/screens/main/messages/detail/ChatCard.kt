package give.away.good.deeds.ui.screens.main.messages.detail


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import give.away.good.deeds.network.model.ChatMessage
import give.away.good.deeds.ui.screens.app_common.ProfileAvatar
import give.away.good.deeds.utils.TimeAgo


@Composable
fun ChatCard(
    chatMessage: ChatMessage,
    userId: String,
    senderProfilePic: String?,
) {
    if (userId == chatMessage.senderId) {
        ChatMyCard(chatMessage)
    } else {
        ChatSenderCard(chatMessage, senderProfilePic)
    }
}

@Composable
fun ChatMyCard(
    chatMessage: ChatMessage
) {
    val radius = 28.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 56.dp),
        horizontalAlignment = Alignment.End
    ) {
        Card(
            shape = RoundedCornerShape(radius, 20.dp, 0.dp, radius)
        ) {
            Text(
                chatMessage.message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = TimeAgo.timeAgo(chatMessage.timestamp.time),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ChatSenderCard(
    chatMessage: ChatMessage,
    senderProfilePic: String?,
) {

    val radius = 28.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 56.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfileAvatar(
                profileUrl = senderProfilePic,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp)),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Card(
                shape = RoundedCornerShape(20.dp, radius, radius, 0.dp)
            ) {
                Text(
                    chatMessage.message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                )
            }
        }

        Box(
            modifier = Modifier.padding(start = 56.dp, top = 4.dp)
        ) {
            Text(
                text = TimeAgo.timeAgo(chatMessage.timestamp.time),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

}