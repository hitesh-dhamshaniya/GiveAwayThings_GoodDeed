package give.away.good.deeds.ui.screens.main.messages.list


import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import give.away.good.deeds.network.model.ChatGroup
import give.away.good.deeds.ui.screens.app_common.EmptyResultStateView
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.TimeAgo
import org.koin.androidx.compose.koinViewModel
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageListScreen(
    navController: NavController? = null,
    viewModel: ChatGroupViewModel = koinViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Messages",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        })
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {

            LaunchedEffect(Unit, block = {
                viewModel.fetchChatGroups()
            })

            val uiState = viewModel.uiState.collectAsState()
            when (val state = uiState.value) {
                is AppState.Result<List<ChatGroup>> -> {
                    MessageList(
                        chatGroups = state.data ?: emptyList(),
                        navController = navController
                    )
                }

                is AppState.Loading -> {
                    LoadingView()
                }

                is AppState.Error -> {
                    when (state.cause) {
                        ErrorCause.NO_INTERNET -> {
                            NoInternetStateView {
                                viewModel.fetchChatGroups()
                            }
                        }

                        ErrorCause.NO_RESULT -> {
                            EmptyResultStateView(
                                title = "No Messages Found",
                                message = "You haven't send item request yet. Once you send a request, message group will be created."
                            )
                        }

                        ErrorCause.UNKNOWN -> {
                            ErrorStateView(
                                title = "Couldn't Load Messages!",
                                message = state.message,
                            ) {
                                viewModel.fetchChatGroups()
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
    }
}

@Composable
fun MessageList(
    chatGroups: List<ChatGroup>,
    navController: NavController? = null,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(chatGroups) { chatGroup ->
            MessageCard(chatGroup, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageCard(
    chatGroup: ChatGroup,
    navController: NavController? = null,
) {
    Card(
        onClick = {
            navController?.navigate("chat")
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = chatGroup.user?.profilePic,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row {
                    Text(
                        chatGroup.user?.getName() ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )

                    Text(
                        text = TimeAgo.timeAgo(chatGroup.chat?.timestamp?.time ?: 0L),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    chatGroup.chat?.message ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

}

private fun getRelativeTime(date: Date?) : String {
    val now = System.currentTimeMillis();
    return DateUtils.getRelativeTimeSpanString(
        date?.time ?: 0,
        now,
        DateUtils.DAY_IN_MILLIS
    ).toString()
}