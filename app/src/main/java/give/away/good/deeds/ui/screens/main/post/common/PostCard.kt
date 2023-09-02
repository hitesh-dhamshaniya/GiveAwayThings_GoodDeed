package give.away.good.deeds.ui.screens.main.post.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.network.model.PostInfo
import give.away.good.deeds.ui.screens.app_common.ProfileAvatar
import give.away.good.deeds.ui.screens.main.post.list.PostImageCarousel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    postInfo: PostInfo,
    onClick: (PostInfo) -> Unit,
    viewModel: PostViewModel = koinViewModel()
) {
    val post = postInfo.post
    val isMyPost = viewModel.isMyPost(post)

    Card (
        onClick = {
            onClick(postInfo)
        }
    ){

        Column {
            Box {
                PostImageCarousel(
                    imageList = post.images,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                )

                if (isMyPost) {
                    PostStatusBadge(
                        post = post,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }


            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                if (!isMyPost)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ProfileAvatar(
                            profileUrl = postInfo.user?.profilePic ?: "",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(24.dp)),
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                postInfo.user?.getName() ?: "",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "0.8 miles away",
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
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PostList(
    postList: List<PostInfo>,
    onClick: (PostInfo) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(postList) { post ->
            PostCard(
                postInfo = post,
                onClick = onClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}

@Composable
fun PostStatusBadge(
    post: Post,
    modifier: Modifier
) {
    val text: String
    val backgroundColor: Color
    if (post.isActive()) {
        text = "Active"
        backgroundColor = Color(0xFF03A9F4)
    } else if (post.isClosed()) {
        text = "Give Away"
        backgroundColor = Color(0xFF009688)
    } else {
        text = "Cancelled"
        backgroundColor = Color(0xFFF44336)
    }

    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        color = Color.White,

        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor.copy(alpha = 0.7f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}