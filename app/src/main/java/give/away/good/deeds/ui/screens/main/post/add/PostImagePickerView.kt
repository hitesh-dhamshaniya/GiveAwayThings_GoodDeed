package give.away.good.deeds.ui.screens.main.post.add

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.dhaval2404.imagepicker.ImagePicker
import give.away.good.deeds.R

@Composable
fun PostImagePickerView(
    imageUris: Set<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
) {
    val context = LocalContext.current as Activity

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
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
        }
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val uriList = imageUris.toList()
        items(uriList) { uri ->
            ImagePreviewCard(uri, onRemove = {
                onRemove.invoke(uri)
            })
        }


        item {
            if (imageUris.size < 5) {
                AddPhotoCard(onClick = {
                    ImagePicker.with(context)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            imagePickerLauncher.launch(intent)
                        }
                })
            } else {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }

}

@Composable
private fun AddPhotoCard(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(imagePickerCardSize)
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_add_photo),
            contentDescription = "",
            modifier = Modifier.size(24.dp)
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Text(
            text = "Add photo",
            fontSize = 14.sp,
            fontWeight = FontWeight.W600
        )
    }
}

private val imagePickerCardSize = 106.dp

@Composable
private fun ImagePreviewCard(imageUri: String, onRemove: () -> Unit) {
    Box {
        Column(
            modifier = Modifier
                .size(imagePickerCardSize)
                .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageUri.startsWith("https")) {
                AsyncImage(
                    model = imageUri,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = Uri.parse(imageUri),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }

        FloatingActionButton(
            onClick = onRemove,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd)
                .size(28.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "",
                modifier = Modifier.size(16.dp)
            )
        }
    }

}


