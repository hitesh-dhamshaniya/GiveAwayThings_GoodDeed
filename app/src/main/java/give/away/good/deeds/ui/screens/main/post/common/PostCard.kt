package give.away.good.deeds.ui.screens.main.post.common

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import give.away.good.deeds.network.model.Post
import give.away.good.deeds.ui.screens.main.post.list.PostImageCarousel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    onClick: (Post) -> Unit,
    viewModel: PostViewModel = koinViewModel()
) {
    val isMyPost = viewModel.isMyPost(post)

    Card (
        onClick = {
            onClick(post)
        }
    ){

        Column {
            PostImageCarousel(
                imageList = post.images,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                if (!isMyPost)
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
    postList: List<Post>,
    onClick: (Post) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(postList) { post ->
            PostCard(
                post = post,
                onClick = onClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}