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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import give.away.good.deeds.network.model.ChatGroupMessage
import give.away.good.deeds.network.model.ChatMessage
import give.away.good.deeds.network.model.User
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import org.koin.androidx.compose.koinViewModel

@Composable
fun MessageDetailScreen(
    groupId: String = "",
    navController: NavController?,
    viewModel: ChatViewModel = koinViewModel()
) {
    LaunchedEffect(Unit, block = {
        viewModel.fetchChatMessages(groupId)
    })

    val uiState = viewModel.uiState.collectAsState()
    when (val state = uiState.value) {
        is AppState.Result<ChatGroupMessage> -> {
            val chatGroupMessage = state.data

            if(chatGroupMessage!=null) {
                LaunchedEffect(Unit, block = {
                    viewModel.setSnapshotListener(chatGroupMessage)
                })
            }

            MessageDetailContainer(
                navController = navController,
                chatGroupMessage = chatGroupMessage,
            )
        }

        is AppState.Loading -> {
            LoadingView()
        }

        is AppState.Error -> {
            when (state.cause) {
                ErrorCause.NO_INTERNET -> {
                    NoInternetStateView {
                        viewModel.fetchChatMessages(groupId)
                    }
                }

                ErrorCause.NO_RESULT -> {
                    ChatView(
                        chatGroupMessage = null
                    )
                }

                ErrorCause.UNKNOWN -> {
                    ErrorStateView(
                        title = "Couldn't Load Messages!",
                        message = state.message,
                    ) {
                        viewModel.fetchChatMessages(groupId)
                    }
                }

                else -> {

                }
            }
        }

        is AppState.Ideal -> {
            // do nothing
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailContainer(
    chatGroupMessage: ChatGroupMessage?,
    navController: NavController?,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = chatGroupMessage?.user?.getName() ?: "",
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
            ChatView(
                chatGroupMessage = chatGroupMessage,
            )
        }
    }
}


@Composable
fun ChatView(
    chatGroupMessage: ChatGroupMessage?,
    viewModel: ChatViewModel = koinViewModel()
) {
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
            val chatMessages = chatGroupMessage?.messageList ?: emptyList()
            items(chatMessages.reversed()) { chatMessage ->
                ChatCard(
                    chatMessage = chatMessage,
                    userId = chatGroupMessage?.me?.id ?: ""
                )
            }
        }

        val message = remember { mutableStateOf("") }
        TextField(
            value = message.value,
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
                    if (chatGroupMessage != null) {
                        viewModel.sendMessage(chatGroupMessage, message.value)
                        message.value = ""
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Send, contentDescription = "Send"
                    )
                }
            })

        Spacer(modifier = Modifier.height(8.dp))
    }
}

