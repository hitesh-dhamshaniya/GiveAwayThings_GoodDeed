package give.away.good.deeds.ui.screens.main.setting.profile

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.github.dhaval2404.imagepicker.ImagePicker
import give.away.good.deeds.R

@Composable
fun ProfileImageView(
    imageUri: String? = null,
    onAdd: (String) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .size(160.dp)
                .border(4.dp, Color.LightGray, RoundedCornerShape(80.dp))
                .clip(RoundedCornerShape(80.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (!imageUri.isNullOrBlank()) {
                AsyncImage(
                    model = if (imageUri.startsWith("http")) imageUri else Uri.parse(imageUri),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_avatar_placeholder),
                    error = painterResource(R.drawable.ic_avatar_placeholder),
                )
            } else {
                Image(
                    painterResource(R.drawable.ic_avatar_placeholder),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEEEEEE)),
                    contentDescription = "",
                )
            }
        }

        val context = LocalContext.current as Activity

        val imagePickerLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(),
                onResult = { result ->
                    val resultCode = result.resultCode
                    val data = result.data

                    if (resultCode == Activity.RESULT_OK) {
                        //Image Uri will not be null for RESULT_OK
                        data?.data?.let { fileUri ->
                            onAdd(fileUri.toString())
                        }
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                })
        FloatingActionButton(
            onClick = {
                ImagePicker.with(context)
                    .compress(512)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080, 1080
                    )  //Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent { intent ->
                        imagePickerLauncher.launch(intent)
                    }
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd)
                .size(48.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_photo),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

