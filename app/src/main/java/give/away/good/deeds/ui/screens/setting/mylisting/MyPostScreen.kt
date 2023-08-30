package give.away.good.deeds.ui.screens.setting.mylisting


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import give.away.good.deeds.ui.screens.post_screens.post.common.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostScreen(
    onBackPress: () -> Unit,
    navigateToDetail: (() -> Unit)? = null,
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
            PostList(navigateToDetail)
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
                isMyPost = true,
                onClick = {
                    navigateToDetail?.invoke()
                }
            )
        }
    }
}