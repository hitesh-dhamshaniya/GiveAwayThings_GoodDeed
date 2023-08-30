package give.away.good.deeds.ui.screens.post_screens.post.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import give.away.good.deeds.ui.screens.app_common.PagerIndicator


@Composable
@OptIn(ExperimentalFoundationApi::class)
fun PostImageCarousel(
    imageList : List<String>,
    modifier: Modifier = Modifier,
) {
    val pageCount = imageList.size
    val pagerState = rememberPagerState { pageCount }
    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState
        ) { pageIndex ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AsyncImage(
                    model = imageList[pageIndex],
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            PagerIndicator(
                pagerState = pagerState,
                indicatorCount = pageCount,
                activeColor = MaterialTheme.colorScheme.primary,
                inActiveColor = Color.White
            )
        }
    }
}