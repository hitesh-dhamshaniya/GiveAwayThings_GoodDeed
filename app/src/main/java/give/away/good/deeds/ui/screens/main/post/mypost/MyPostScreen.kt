package give.away.good.deeds.ui.screens.main.post.mypost


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.ui.screens.app_common.ErrorStateView
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.NoResultStateView
import give.away.good.deeds.ui.screens.main.post.common.PostCard
import give.away.good.deeds.ui.screens.main.post.common.PostState
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostScreen(
    onBackPress: () -> Unit,
    navigateToDetail: (() -> Unit)? = null,
    viewModel: MyPostViewModel = koinViewModel()
) {
    Scaffold(topBar = {
        MediumTopAppBar(
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
                        viewModel.fetchPosts()
                    }
                }

                is PostState.Empty -> {
                    NoResultStateView {
                        viewModel.fetchPosts()
                    }
                }

                is PostState.Error -> {
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
                isMyPost = true,
                onClick = {
                    navigateToDetail?.invoke()
                }
            )
        }
    }
}