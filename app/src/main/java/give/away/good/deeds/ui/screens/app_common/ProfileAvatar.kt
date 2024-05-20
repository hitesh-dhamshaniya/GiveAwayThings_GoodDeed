package give.away.good.deeds.ui.screens.app_common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import give.away.good.deeds.R

@Composable
fun ProfileAvatar(
    modifier: Modifier,
    profileUrl: String? = null,
) {
    AsyncImage(
        model = profileUrl,
        modifier = modifier,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.ic_avatar_placeholder),
        error = painterResource(R.drawable.ic_avatar_placeholder),
    )
}