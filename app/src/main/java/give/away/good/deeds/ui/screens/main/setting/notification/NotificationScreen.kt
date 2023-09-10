package give.away.good.deeds.ui.screens.main.setting.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Notification
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.repository.NOTIFICATION_TYPE_MESSAGE
import give.away.good.deeds.repository.NOTIFICATION_TYPE_POST
import give.away.good.deeds.ui.screens.app_common.EmptyResultStateView
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.ProfileAvatar
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.utils.TimeAgo
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = koinViewModel()

) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Arrow"
                    )
                }
            },
        )
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {

            LaunchedEffect(Unit, block = {
                viewModel.fetchNotifications()
            })

            val uiState = viewModel.uiState.collectAsState()
            when (val state = uiState.value) {
                is AppState.Result<List<Notification>> -> {
                    NotificationList(
                        lists = state.data ?: emptyList(),
                        navController = navController,
                    )
                }

                is AppState.Loading -> {
                    LoadingView()
                }

                is AppState.Error -> {
                    when (state.cause) {
                        ErrorCause.NO_INTERNET -> {
                            NoInternetStateView {
                                viewModel.fetchNotifications()
                            }
                        }

                        ErrorCause.NO_RESULT -> {
                            EmptyResultStateView(
                                title = "No Notifications Found!",
                                message = "You don't have any notifications yet."
                            )
                        }

                        ErrorCause.UNKNOWN -> {
                            ErrorStateView(
                                title = "Couldn't Load Notifications!",
                                message = "We are not able to load your notifications. Please try again later",
                            ) {
                                viewModel.fetchNotifications()
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
private fun NotificationList(
    lists: List<Notification>,
    navController: NavController
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(lists) { notification ->
            NotificationCard(notification, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationCard(
    notification: Notification,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            when (notification.type) {
                NOTIFICATION_TYPE_MESSAGE -> {
                    if (!notification.data.isNullOrBlank()) {
                        navController.navigate("chat/" + notification.data)
                    }
                }

                NOTIFICATION_TYPE_POST -> {
                    if (!notification.data.isNullOrBlank()) {
                        val post = Gson().fromJson(notification.data, Post::class.java)
                        navController.navigate("post_detail/" + post.id)
                    }
                }

                else -> {

                }
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                NotificationIcon(
                    notification = notification,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    notification.title,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(4.dp))

                if(!notification.image.isNullOrBlank()){
                    Spacer(modifier = Modifier.height(4.dp))

                    AsyncImage(
                        model = notification.image,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = TimeAgo.timeAgo(notification.time.time ?: 0L),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}


@Composable
private fun NotificationIcon(
    notification: Notification,
    modifier: Modifier = Modifier
) {
    when (notification.type) {
        NOTIFICATION_TYPE_MESSAGE -> {
            ProfileAvatar(modifier = modifier.size(48.dp), profileUrl = notification.icon)
        }

        NOTIFICATION_TYPE_POST -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_volunteer),
                contentDescription = "",
                modifier = modifier
            )
        }

        else -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_notifications),
                contentDescription = "",
                modifier = modifier
            )
        }
    }
}