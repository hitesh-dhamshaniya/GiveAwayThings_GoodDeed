package give.away.good.deeds.ui.screens.main.post.list


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.network.model.PostInfo
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.NoResultStateView
import give.away.good.deeds.ui.screens.main.post.common.PostList
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    onPostClick: ((Post) -> Unit)? = null,
    viewModel: PostListViewModel = koinViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            LaunchedEffect(Unit, block = {
                viewModel.fetchPosts()
            })

            val uiState = viewModel.uiState.collectAsState()

            when (val state = uiState.value) {
                is AppState.Result<List<PostInfo>> -> {
                    PostList(
                        postList = state.data ?: emptyList(),
                        onClick = { post ->
                            onPostClick?.invoke(post.post)
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
                            NoResultStateView(
                                title = "No Give Away Things",
                                message = "Sorry, We couldn't find any give away items for you. Please try again after some time."
                            ) {
                                viewModel.fetchPosts()
                            }
                        }
                        ErrorCause.UNKNOWN -> {
                            ErrorStateView(
                                title = "Couldn't Load Posts!",
                                message = state.message,
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



@Composable
@Preview
fun PostListScreenPreview() {
    AppTheme {
        PostListScreen()
    }
}