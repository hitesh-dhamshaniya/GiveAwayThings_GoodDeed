package give.away.good.deeds.ui.screens.app_common

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
/**
 * Composable functions for Lottie Animations
 * @author Hitesh
 * @since 02.09.2023
 */
@Composable
fun LottieAnimationView(
    @RawRes resId: Int
) {
    // R.raw.lottie_animation_llxxvzkf
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
    }
}