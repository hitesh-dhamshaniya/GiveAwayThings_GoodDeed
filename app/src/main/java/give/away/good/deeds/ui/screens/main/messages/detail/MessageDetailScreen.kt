package give.away.good.deeds.ui.screens.main.messages.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailScreen(
    navController: NavController?,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "David Warner",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController?.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = "Back Arrow"
                    )
                }
            },
        )
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            ChatView()
        }
    }
}


@Composable
fun ChatView() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            reverseLayout = true
        ) {
            items(8) {
                ChatCard((it % 2) == 0)
            }
        }

        val message = remember { mutableStateOf("") }
        TextField(value = message.value,
            onValueChange = {
                message.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            shape = RoundedCornerShape(8.dp, 8.dp),
            placeholder = {
                Text("Message")
            },
            trailingIcon = {
                IconButton(onClick = {
                    message.value = ""
                }) {
                    Icon(
                        imageVector = Icons.Filled.Send, contentDescription = "Send"
                    )
                }
            })

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ChatCard(
    isMyMessage: Boolean
) {
    val modifier =
        if (isMyMessage) Modifier.padding(end = 64.dp) else Modifier.padding(start = 64.dp)
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
                "Can I have this item? I will pick up on your availability.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Text(
                "Yesterday",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}