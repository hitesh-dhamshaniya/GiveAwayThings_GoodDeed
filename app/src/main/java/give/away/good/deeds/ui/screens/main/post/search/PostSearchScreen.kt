package give.away.good.deeds.ui.screens.main.post.search

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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import give.away.good.deeds.R
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.ui.screens.app_common.EmptyResultStateView
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.LottieAnimationView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.NoResultStateView
import give.away.good.deeds.ui.screens.main.post.common.PostCard
import give.away.good.deeds.ui.screens.main.post.common.PostState
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun PostSearchScreen(
    navigateToDetail: (() -> Unit)? = null,
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
                is PostState.Result<List<Post>> -> {
                    PostList(
                        postList = state.data ?: emptyList(),
                        navigateToDetail = navigateToDetail,
                    )
                }

                is PostState.Loading -> {
                    LoadingView()
                }

                is PostState.NoInternet -> {
                    NoInternetStateView {
                        viewModel.searchPosts(text)
                    }
                }

                is PostState.Empty -> {
                    EmptyResultStateView()
                }

                is PostState.Error -> {
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
fun LottieAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_animation_llxxvzkf))
    Box(
        modifier = Modifier.padding(64.dp)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
    }
}
