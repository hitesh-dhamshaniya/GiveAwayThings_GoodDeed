package give.away.good.deeds.ui.screens.post_screens.post.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import give.away.good.deeds.ui.screens.post_screens.post.common.PostCard

@Composable
fun PostSearchScreen(
) {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                placeholder = {
                    Text("Search posts")
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search"
                    )
                },
            )

            if (text.isBlank()) {
                LottieAnimation()
            } else {

                Spacer(modifier = Modifier.height(16.dp))

                PostList(
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PostList(
    modifier: Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(5) {
            PostCard()
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
