package give.away.good.deeds.ui.screens.main.post.mypost


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.ui.screens.app_common.EmptyResultStateView
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.main.post.common.PostList
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostScreen(
    onBackPress: () -> Unit,
    onPostClick: ((Post) -> Unit)? = null,
    viewModel: MyPostViewModel = koinViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "My Posts",
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
                .fillMaxSize()
        ) {

            LaunchedEffect(Unit, block = {
                viewModel.fetchPosts()
            })

            val uiState = viewModel.uiState.collectAsState()
            when (val state = uiState.value) {
                is AppState.Result<List<Post>> -> {
                    PostList(
                        postList = state.data ?: emptyList(),
                        onClick = { post ->
                            onPostClick?.invoke(post)
                        },
                    )
                }

                is AppState.Loading -> {
                    LoadingView()
                }

                is AppState.Error -> {
                    when(state.cause){
                        ErrorCause.NO_INTERNET -> {
                            NoInternetStateView {
                                viewModel.fetchPosts()
                            }
                        }
                        ErrorCause.NO_RESULT -> {
                            EmptyResultStateView(
                                title = "No Give Away Things",
                                message = "You haven't created any post yet. Once you create a post it will be visible here."
                            )
                        }
                        ErrorCause.UNKNOWN -> {
                            ErrorStateView(
                                title = "Couldn't Load Posts!",
                                message = "We are not able to load your posts. Please try again later",
                            ) {
                                viewModel.fetchPosts()
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
