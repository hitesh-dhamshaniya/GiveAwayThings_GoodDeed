package give.away.good.deeds.ui.screens.main.post.list


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import give.away.good.deeds.ui.screens.main.post.common.PostCard
import give.away.good.deeds.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navigateToDetail: (() -> Unit)? = null,
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
            PostList(
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
fun PostList(
    navigateToDetail: (() -> Unit)? = null,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(5) {
            PostCard(
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