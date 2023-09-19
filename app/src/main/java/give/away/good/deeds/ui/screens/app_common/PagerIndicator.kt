package give.away.good.deeds.ui.screens.app_common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
/**
 * Pager Indicator
 * @author Hitesh
 * @since 02.09.2023
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    pagerState: PagerState,
    indicatorCount: Int,
    indicatorSize: Dp = 12.dp,
    indicatorShape: Shape = CircleShape,
    space: Dp = 8.dp,
    activeColor: Color = Color(0xffEC407A),
    inActiveColor: Color = Color.LightGray
) {

    val listState = rememberLazyListState()

    val totalWidth: Dp = indicatorSize * indicatorCount + space * (indicatorCount - 1)
    val widthInPx = LocalDensity.current.run { indicatorSize.toPx() }

    val currentItem by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }

    LaunchedEffect(key1 = currentItem) {
        val viewportSize = listState.layoutInfo.viewportSize
        listState.animateScrollToItem(
            currentItem,
            (widthInPx / 2 - viewportSize.width / 2).toInt()
        )
    }

    LazyRow(
        modifier = Modifier.width(totalWidth),
        state = listState,
        contentPadding = PaddingValues(vertical = space),
        horizontalArrangement = Arrangement.spacedBy(space),
        userScrollEnabled = false
    ) {

        items(indicatorCount) { index ->
            val isSelected = (index == currentItem)
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val scale = if (isSelected) {
                            1f
                        } else {
                            .6f
                        }
                        scaleX = scale
                        scaleY = scale

                    }
                    .clip(indicatorShape)
                    .size(indicatorSize)
                    .background(
                        if (isSelected) activeColor else inActiveColor,
                        indicatorShape
                    )
            )
        }
    }
}