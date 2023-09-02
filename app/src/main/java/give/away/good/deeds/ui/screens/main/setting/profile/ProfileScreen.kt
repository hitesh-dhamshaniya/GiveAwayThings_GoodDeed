package give.away.good.deeds.ui.screens.main.setting.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.ui.screens.app_common.NoInternetStateView
import give.away.good.deeds.ui.screens.app_common.SimpleTextFieldView
import give.away.good.deeds.ui.screens.app_common.StateView
import give.away.good.deeds.ui.screens.app_common.StateViewState
import give.away.good.deeds.ui.screens.main.setting.common.SettingState
import give.away.good.deeds.ui.screens.main.setting.location.LoadingView
import give.away.good.deeds.ui.screens.state.AppState
import give.away.good.deeds.ui.screens.state.ErrorCause
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.ui.theme.AppThemeButtonShape
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackPress: () -> Unit,
) {
    Scaffold(topBar = {
        MediumTopAppBar(
            title = {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Arrow"
                    )
                }
            },
        )
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            ProfileFormStateView(
                onBackPress = onBackPress
            )
        }
    }
}

@Composable
fun ProfileFormStateView(
    onBackPress: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {

    LaunchedEffect(Unit, block = {
        viewModel.fetchUser()
    })

    val uiState = viewModel.uiState.collectAsState()

    when(val state = uiState.value){
        is AppState.Result<Unit> -> {
            StateView(
                title = "Success!",
                message = "Profile updated successfully.",
                actionText = "Done",
                type = StateViewState.SUCCESS,
                actionClick = {
                    onBackPress()
                }
            )
        }
        is AppState.Loading -> {
            LoadingView()
        }
        is AppState.Ideal -> {
            ProfileForm()
        }
        is AppState.Error -> {
            when(state.cause){
                ErrorCause.NO_INTERNET -> {
                    NoInternetStateView {
                        viewModel.fetchUser()
                    }
                }
                ErrorCause.UNKNOWN -> {
                    StateView(
                        title = "Failure!",
                        message = state.message,
                        actionText = "Try Again",
                        type = StateViewState.FAILURE,
                        actionClick = {
                            viewModel.fetchUser()
                        }
                    )
                }
                else -> {

                }
            }
        }
    }
}

@Composable
fun ProfileForm(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val formState = viewModel.formState
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ProfileImageView(
            imageUri = formState["profilePic"],
            onAdd = {
                formState.remove("profilePic")
                formState["profilePic"] = it
            }
        )

        val fNameError = remember { mutableStateOf("") }
        SimpleTextFieldView(
            text = "First name",
            value = formState["firstName"] ?: "",
            onValueChange = {
                fNameError.value = ""
                formState.remove("firstName")
                formState["firstName"] = it
            },
            isError = fNameError.value.isNotBlank(),
            supportingText = {
                Text(text = fNameError.value)
            }
        )

        val lNameError = remember { mutableStateOf("") }
        SimpleTextFieldView(
            text = "Last name",
            value = formState["lastName"] ?: "",
            onValueChange = {
                lNameError.value = ""
                formState.remove("lastName")
                formState["lastName"] = it
            },
            isError = lNameError.value.isNotBlank(),
            supportingText = {
                Text(text = lNameError.value)
            }
        )

        SimpleTextFieldView(
            text = "Email address",
            value = formState["email"] ?: "",
            onValueChange = {
            },
            isEnabled = false
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = AppThemeButtonShape,
            onClick = {
                var isError = false
                if (formState["firstName"].isNullOrBlank()) {
                    fNameError.value = "Please enter first name"
                    isError = true
                }
                if (formState["lastName"].isNullOrBlank()) {
                    lNameError.value = "Please enter last name"
                    isError = true
                }
                if (!isError) {
                    viewModel.updateProfile(
                        formState["id"] ?: "",
                        formState["firstName"] ?: "",
                        formState["lastName"] ?: "",
                        formState["profilePic"]
                    )
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

@Preview
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            onBackPress = {

            }
        )
    }
}