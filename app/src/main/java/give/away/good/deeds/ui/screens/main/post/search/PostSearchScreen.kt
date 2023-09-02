package give.away.good.deeds.ui.screens.main.post.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.network.model.PostInfo
import give.away.good.deeds.ui.screens.app_common.EmptyResultStateView
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.LottieAnimationView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.main.post.common.PostList
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostSearchScreen(
    onPostClick: ((Post) -> Unit)? = null,
    viewModel: PostSearchViewModel = koinViewModel()
 ) {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.searchPosts(it)
                },
                placeholder = {
                    Text("Search give away")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search"
                    )
                },
            )

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
                                viewModel.searchPosts(text)
                            }
                        }
                        ErrorCause.NO_RESULT -> {
                            EmptyResultStateView()
                        }
                        ErrorCause.UNKNOWN -> {
                            ErrorStateView(
                                title = "Couldn't Search Posts!",
                                message = state.message,
                            ) {
                                viewModel.searchPosts(text)
                            }
                        }
                        else -> {
                            LottieAnimationView(
                                resId = R.raw.lottie_animation_llxxvzkf
                            )
                        }
                    }
                }
                is AppState.Ideal -> {
                    LottieAnimationView(
                        resId = R.raw.lottie_animation_llxxvzkf
                    )
                }
            }
        }
    }
}

