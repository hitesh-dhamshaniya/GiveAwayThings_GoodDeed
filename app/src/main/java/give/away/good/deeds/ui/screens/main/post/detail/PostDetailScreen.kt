package give.away.good.deeds.ui.screens.main.post.detail

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.LottieAnimationView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.SimpleAlertDialog
import give.away.good.deeds.ui.screens.main.post.list.PostImageCarousel
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.ui.theme.AppThemeButtonShape
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: String,
    onBackPress: () -> Unit,
    viewModel: PostDetailViewModel = koinViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Post Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
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
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            LaunchedEffect(Unit, block = {
                viewModel.getPost(postId)
            })

            val uiState = viewModel.uiState.collectAsState()
            when (val state = uiState.value) {
                is AppState.Result<Post> -> {
                    val post = state.data
                    if (post == null) {
                        LaunchedEffect(Unit) {
                            onBackPress()
                        }
                    } else {
                        PostDetailActionView(
                            post = post,
                            onBackPress = onBackPress
                        )
                    }
                }

                is AppState.Loading -> {
                    LoadingView()
                }

                is AppState.Error -> {
                    when (state.cause) {
                        ErrorCause.NO_INTERNET -> {
                            NoInternetStateView {
                                viewModel.getPost(postId)
                            }
                        }

                        ErrorCause.UNKNOWN -> {
                            ErrorStateView(
                                title = "Couldn't Load Post!",
                                message = state.message,
                            ) {
                                viewModel.getPost(postId)
                            }
                        }

                        else -> {
                            LottieAnimationView(
                                resId = R.raw.animation_no_result
                            )
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
fun PostDetailActionView(
    post: Post,
    onBackPress: () -> Unit,
    viewModel: PostDetailViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                PostDetailView(post)
            }

            val location = post.location
            if (location != null) {
                item {
                    GoogleMapView(
                        defaultLatLng = post.location
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(96.dp))
            }
        }

        val showGiveAwayDialog = remember { mutableStateOf(false) }
        if (showGiveAwayDialog.value)
            SimpleAlertDialog(
                title = "Did you give away to someone?",
                message = "Are you sure you want to close give away?\n\nChoose YES option if you've given away items to community members.",
                confirmAction = "Yes",
                dismissAction = "No",
                onDismiss = {
                    showGiveAwayDialog.value = false
                },
                onConfirm = {
                    viewModel.setPostStatus(post, 0)
                }
            )

        val showCloseDialog = remember { mutableStateOf(false) }
        if (showCloseDialog.value)
            SimpleAlertDialog(
                title = "Is your item no longer available for give away?",
                message = "Are you sure you want to cancel give away?\nChoose YES option if item is no longer available for give away.",
                confirmAction = "Yes",
                dismissAction = "No",
                onDismiss = {
                    showCloseDialog.value = false
                },
                onConfirm = {
                    viewModel.setPostStatus(post, -1)
                }
            )

        if (viewModel.isMyPost(post)) {
            if (!post.isClosed() && !post.isCancelled()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 16.dp),
                        shape = AppThemeButtonShape,
                        onClick = {
                            showGiveAwayDialog.value = true
                        },
                    ) {
                        Text(
                            text = "Done".uppercase(),
                            modifier = Modifier.padding(8.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 16.dp),
                        shape = AppThemeButtonShape,
                        onClick = {
                            showCloseDialog.value = true
                        },
                    ) {
                        Text(
                            text = "Cancel".uppercase(),
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }
            }
        } else {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                shape = AppThemeButtonShape,
                onClick = {
                },
            ) {
                Text(
                    text = "Request Item",
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@Composable
fun PostDetailView(
    post: Post,
    viewModel: PostDetailViewModel = koinViewModel()
) {
    Card {
        Column {
            PostImageCarousel(
                imageList = post.images,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1554151228-14d9def656e4?w=512&q=80",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            "David Warner",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            "Added 19 hours ago",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    post.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    post.description,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun GoogleMapView(
    defaultLatLng: LatLng,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 14f)
    }

    Card(
    ) {
        Column {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Marker(
                    state = MarkerState(position = defaultLatLng),
                    snippet = "Post location",
                )
            }

            Row(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        "49 Featherstone Street, EC1Y 8SY, London",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        "0.8 miles away",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }


        }
    }

}