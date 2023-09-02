package give.away.good.deeds.ui.screens.main.post.list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.NoResultStateView
import give.away.good.deeds.ui.screens.main.post.add.AddPostForm
import give.away.good.deeds.ui.screens.main.post.common.PostCard
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navigateToDetail: (() -> Unit)? = null,
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
                is AppState.Result<List<Post>> -> {
                    PostList(
                        postList = state.data ?: emptyList(),
                        navigateToDetail = navigateToDetail,
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
                                title = "No Give Away Things Nearby",
                                message = "Sorry, We couldn't find any give away items in your nearby location. Please try again after some time."
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
                    AddPostForm()
                }
            }
        }
    }
}

@Composable
fun PostList(
    postList: List<Post>,
    navigateToDetail: (() -> Unit)? = null,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(postList) { post ->
            PostCard(
                post = post,
                isMyPost = false,
                onClick = {
                    navigateToDetail?.invoke()
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
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