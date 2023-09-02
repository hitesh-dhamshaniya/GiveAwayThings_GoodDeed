package give.away.good.deeds.ui.screens.app_common

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import give.away.good.deeds.R
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.ui.theme.AppThemeButtonShape

enum class StateViewState {
    SUCCESS,
    FAILURE,
    NO_RESULT,
    NO_INTERNET
}

@Composable
fun StateView(
    title: String? = null,
    message: String? = null,
    actionText: String? = null,
    type: StateViewState = StateViewState.SUCCESS,
    actionClick: (() -> Unit)? = null,
    @DrawableRes icon: Int? = null,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {

        Image(
            painter = painterResource(id = icon ?: getTypeIcon(type)),
            contentDescription = "",
            modifier = Modifier.size(180.dp),
            colorFilter = ColorFilter.tint(getTypeTint(type))
        )


        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium
            )
        }


        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Normal
            )

        }

        if (actionText != null) {
            Spacer(modifier = Modifier.height(8.dp))
             Button(
                shape = AppThemeButtonShape,
                onClick = {
                    actionClick?.invoke()
                },
                 modifier = Modifier.defaultMinSize(minWidth = 120.dp)
            ) {
                Text(
                    text = actionText.uppercase(),
                    modifier = Modifier.padding(8.dp),
                )
            }
        }

    }

}

@Composable
fun StateViewWithLottie(
    @RawRes resId: Int? = null,
    title: String? = null,
    message: String? = null,
    actionText: String? = null,
    actionClick: (() -> Unit)? = null,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {

        if(resId !=null) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(180.dp),
            )
        }

        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium
            )
        }


        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Normal
            )

        }

        if (actionText != null) {
            Spacer(modifier = Modifier.height(8.dp))
             Button(
                shape = AppThemeButtonShape,
                onClick = {
                    actionClick?.invoke()
                },
                 modifier = Modifier.defaultMinSize(minWidth = 120.dp)
            ) {
                Text(
                    text = actionText.uppercase(),
                    modifier = Modifier.padding(8.dp),
                )
            }
        }

    }

}

private fun getTypeTint(type: StateViewState): Color {
    return when (type) {
        StateViewState.SUCCESS -> Color(0XFF00AA8D)
        StateViewState.FAILURE -> Color(0XFFF44336)
        StateViewState.NO_RESULT -> Color(0XFFF44336)
        StateViewState.NO_INTERNET -> Color(0XFF039BE5)
    }
}

private fun getTypeIcon(type: StateViewState): Int {
    return when (type) {
        StateViewState.SUCCESS -> R.drawable.ic_success_24px
        StateViewState.FAILURE -> R.drawable.ic_error_24px
        StateViewState.NO_INTERNET -> R.drawable.ic_no_network_24px
        StateViewState.NO_RESULT -> R.drawable.ic_no_network_24px
    }
}


@Composable
fun NoInternetStateView(
    onAction: () -> Unit
) {
    StateViewWithLottie(
        resId = R.raw.animation_no_internet,
        title = "No Internet Connection",
        message = "It seems that you're not online. Try again.",
        actionText = "Try Again",
        actionClick = onAction
    )
}

@Composable
fun NoResultStateView(
    title: String? = null,
    message: String? = null,
    onAction: () -> Unit
) {
    StateViewWithLottie(
        resId = R.raw.animation_no_result,
        title = title ?: "No Result Found",
        message = message ?: "Sorry, We couldn't find what you're looking for. Please try again.",
        actionText = "Try Again",
        actionClick = onAction
    )
}

@Composable
fun EmptyResultStateView(
    title: String? = null,
    message: String? = null,
) {
    StateViewWithLottie(
        resId = R.raw.animation_no_result,
        title = title ?: "No Result Found",
        message = message ?: "Sorry, We couldn't find what you're looking for. Please try again later.",
    )
}

@Composable
fun ErrorStateView(
    title: String? = null,
    message: String? = null,
    onAction: () -> Unit
) {
    StateViewWithLottie(
        resId = R.raw.animation_error,
        title = title,
        message = message,
        actionText = "Try Again",
        actionClick = onAction
    )
}

@Composable
fun SuccessStateView(
    title: String? = null,
    message: String? = null,
    onAction: () -> Unit
) {
    StateViewWithLottie(
        resId = R.raw.animation_success,
        title = title,
        message = message,
        actionText = "Try Again",
        actionClick = onAction
    )
}

@Preview
@Composable
fun StateViewPreview() {

    AppTheme {

        StateView(
            title = "Something went wrong!",
            message = "Failed to change the password. Please try again.",
            actionText = "Try Again",
            type = StateViewState.SUCCESS
        )

    }

}