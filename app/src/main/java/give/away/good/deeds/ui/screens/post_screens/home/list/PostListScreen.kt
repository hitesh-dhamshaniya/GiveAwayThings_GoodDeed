package give.away.good.deeds.ui.screens.post_screens.home.list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
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
            PostList()
        }
    }
}

@Composable
fun PostList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        items(5) {
            PostCard()
        }

    }
}

@Composable
fun PostCard(
) {
    Card {
        val list = listOf<String>(
            "https://images.unsplash.com/photo-1551298370-9d3d53740c72?&w=1000&q=80",
            "https://images.unsplash.com/photo-1618220179428-22790b461013?&w=1000&q=80",
            "https://images.unsplash.com/photo-1618220179428-22790b461013?w=1000&q=80"
        )
        Column {
            PostImageCarousel(
                imageList = list,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

        }
    }
}


@Composable
@Preview
fun PostListScreenPreview() {
    PostListScreen()
}