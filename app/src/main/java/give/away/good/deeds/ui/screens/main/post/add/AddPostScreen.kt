@file:OptIn(ExperimentalMaterial3Api::class)

package give.away.good.deeds.ui.screens.main.post.add


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.ui.screens.app_common.SimpleTextFieldView
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.ui.theme.AppThemeButtonShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Create Post",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            AddPostForm()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostForm() {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Column {
                Text(
                    text = "Add details of give away things",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            Column {
                Text(
                    text = "Add up to 5 images of the item",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(4.dp))

                val images = remember { mutableStateOf(setOf<String>()) }
                PostImagePickerView(
                    imageUris = images.value,
                    onAdd = {
                        val newImages = images.value.toMutableSet()
                        newImages.add(it)
                        images.value = newImages
                    },
                    onRemove = {
                        val newImage = images.value.toMutableSet()
                        newImage.remove(it)
                        images.value = newImage
                    },
                )
            }
        }

        item {
            val title = remember { mutableStateOf("") }
            SimpleTextFieldView(
                text = "Title of the item",
                value = title.value,
                onValueChange = {
                    title.value = it
                }
            )
        }

        item {
            val description = remember { mutableStateOf("") }
            SimpleTextFieldView(
                text = "Description of the give away items",
                value = description.value,
                minLines = 3,
                onValueChange = {
                    description.value = it
                },
                maxLines = 5,
                supportingText = {
                    Text("Note: Add expiry & allergy details if applicable")
                }
            )
        }

        item {
            Column {
                Text(
                    text = "Quantity",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                )

                Row {
                    val chip = remember { mutableStateOf("") }
                    repeat(5) { count ->
                        val displayText = (count + 1).toString()
                        FilterChip(
                            selected = chip.value == displayText,
                            onClick = {
                                chip.value = displayText
                            },
                            label = { Text(displayText) },
                        )
                    }
                }

            }
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = AppThemeButtonShape,
                onClick = {

                },
            ) {
                Text(
                    text = "Save",
                    modifier = Modifier.padding(8.dp),
                )
            }
        }

    }
}

@Preview
@Composable
fun AddPostFormPreview() {
    AppTheme {
        AddPostForm()
    }
}