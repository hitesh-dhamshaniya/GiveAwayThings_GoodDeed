package give.away.good.deeds.ui.screens.main.post.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import give.away.good.deeds.ui.screens.main.post.list.PostImageCarousel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    isMyPost: Boolean = false,
    onClick: () -> Unit,
) {
    Card (
        onClick = onClick
    ){
        val list = listOf<String>(
            "https://images.unsplash.com/photo-1551298370-9d3d53740c72?&w=1000&q=80",
            "https://images.unsplash.com/photo-1618220179428-22790b461013?&w=1000&q=80",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=1000&q=80"
        )
        Column {
            PostImageCarousel(
                imageList = list,
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
                    "Home Furniture Giveaway",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Are you looking to refresh your living space or simply have some furniture that needs a new home? We have a collection of gently used home furniture items that we're giving away for free! Our well-maintained pieces are in good condition and can add comfort and style to your home.",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))


            }
        }
    }
}
