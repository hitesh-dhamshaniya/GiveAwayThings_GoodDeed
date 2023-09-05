package give.away.good.deeds.ui.screens.main.post.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ImageFullScreenView(
    imageUrl: String
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = imageUrl,
            modifier = Modifier.fillMaxSize(),
            contentDescription = "",
            contentScale = ContentScale.Fit
        )

        /*IconButton(
            modifier = Modifier.padding(16.dp),
            onClick = onClose
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close"
            )
        }*/

    }
}