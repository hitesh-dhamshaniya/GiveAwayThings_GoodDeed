package give.away.good.deeds.ui.screens.app_common

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.R
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.ui.theme.AppThemeButtonShape

enum class StateViewState {
    SUCCESS,
    FAILURE,
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

private fun getTypeTint(type: StateViewState): Color {
    return when (type) {
        StateViewState.SUCCESS -> Color(0XFF00AA8D)
        StateViewState.FAILURE -> Color(0XFFF44336)
        StateViewState.NO_INTERNET -> Color(0XFF039BE5)
        else -> Color(0XFF9E9E9E)
    }
}

private fun getTypeIcon(type: StateViewState): Int {
    return when (type) {
        StateViewState.SUCCESS -> R.drawable.ic_success_24px
        StateViewState.FAILURE -> R.drawable.ic_error_24px
        StateViewState.NO_INTERNET -> R.drawable.ic_no_network_24px
        else -> R.drawable.ic_success_24px
    }
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