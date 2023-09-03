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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.SimpleTextFieldView
import give.away.good.deeds.ui.screens.app_common.StateView
import give.away.good.deeds.ui.screens.app_common.StateViewState
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.ui.theme.AppThemeButtonShape
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    viewModel: AddPostViewModel = koinViewModel()
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

            LaunchedEffect(Unit, block = {
                viewModel.fetchLocation()
            })

            val uiState = viewModel.uiState.collectAsState()
            when(val state = uiState.value){
                is AppState.Result<Unit> -> {
                    StateView(
                        title = "Post Created!",
                        message = "Thank you for being a part of our wonderful community.",
                        actionText = "Done",
                        type = StateViewState.SUCCESS,
                        actionClick = {
                            viewModel.resetState()
                        }
                    )
                }
                is AppState.Loading -> {
                    LoadingView()
                }
                is AppState.Error -> {
                    when(state.cause){
                        ErrorCause.NO_INTERNET -> {
                            NoInternetStateView {
                                viewModel.resetNetworkState()
                            }
                        }
                        ErrorCause.UNKNOWN -> {
                            StateView(
                                title = "Failure!",
                                message = state.message,
                                actionText = "Try Again",
                                type = StateViewState.FAILURE,
                                actionClick = {
                                    viewModel.resetNetworkState()
                                }
                            )
                        }
                        else -> {

                        }
                    }
                }
                is AppState.Ideal -> {
                    AddPostForm()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostForm(
    viewModel: AddPostViewModel = koinViewModel()
) {

    val errorEnabled = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Column {
                Text(
                    text = "Add details of give away things",
                    style = MaterialTheme.typography.titleMedium,
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

                PostImagePickerView(
                    imageUris = viewModel.post.images.toSet(),
                    onAdd = {
                        val newImages = viewModel.post.images.toMutableList()
                        newImages.add(it)
                        viewModel.onImagesChange(newImages)
                    },
                    onRemove = {
                        val newImages = viewModel.post.images.toMutableList()
                        newImages.remove(it)
                        viewModel.onImagesChange(newImages)
                    },
                )

                if (errorEnabled.value && viewModel.post.images.isEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Please select at least 1 image",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        item {
            SimpleTextFieldView(
                text = "Title of the item",
                value = viewModel.post.title,
                onValueChange = {
                    viewModel.onTitleChange(it)
                },
                isError = errorEnabled.value && viewModel.post.title.isBlank(),
                supportingText = {
                    if (errorEnabled.value && viewModel.post.title.isBlank()) {
                        Text("Please enter give away item title")
                    }
                }
            )
        }

        item {
            SimpleTextFieldView(
                text = "Description of the give away items",
                value = viewModel.post.description,
                minLines = 3,
                onValueChange = {
                    viewModel.onDescriptionChange(it)
                },
                maxLines = 5,
                isError = errorEnabled.value && viewModel.post.description.isBlank(),
                supportingText = {
                    if (errorEnabled.value && viewModel.post.description.isBlank()) {
                        Text("Please enter give away item description")
                    } else {
                        Text("Note: Add expiry & allergy details if applicable")
                    }
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
                    repeat(5) { count ->
                        val quantity = (count + 1)
                        FilterChip(
                            selected = viewModel.post.quantity == quantity,
                            onClick = {
                                viewModel.onQuantityChange(quantity)
                            },
                            label = { Text(quantity.toString()) },
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
                    errorEnabled.value = !viewModel.isValidPost()
                    if (!errorEnabled.value) {
                        viewModel.createPost()
                    }
                },
            ) {
                Text(
                    text = "Save".uppercase(),
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