package give.away.good.deeds.ui.screens.setting.changepassword

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.ui.screens.app_common.PasswordTextField
import give.away.good.deeds.ui.screens.app_common.STATE_VIEW_FAILURE
import give.away.good.deeds.ui.screens.app_common.STATE_VIEW_SUCCESS
import give.away.good.deeds.ui.screens.app_common.StateView
import give.away.good.deeds.ui.screens.setting.common.SettingState
import give.away.good.deeds.ui.screens.setting.location.LoadingView
import give.away.good.deeds.ui.theme.AppTheme
import give.away.good.deeds.ui.theme.AppThemeButtonShape
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onBackPress: () -> Unit,
) {
    Scaffold(topBar = {
        MediumTopAppBar(
            title = {
                Text(
                    text = "Change Password",
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
            ChangePasswordStateView(onBackPress)
        }
    }
}

@Composable
fun ChangePasswordStateView(
    onBackPress: () -> Unit,
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    when(val state = uiState.value){
        is SettingState.Result<Unit> -> {
            StateView(
                title = "Success!",
                message = "Password changed successfully!",
                actionText = "Done",
                type = STATE_VIEW_SUCCESS,
                actionClick = {
                    onBackPress()
                }
            )
        }
        is SettingState.Loading -> {
            LoadingView()
        }
        is SettingState.Error -> {
            StateView(
                title = "Failure!",
                message = state.message,
                actionText = "Try Again",
                type = STATE_VIEW_FAILURE,
                actionClick = {
                    viewModel.resetState()
                }
            )
        }
        is SettingState.None -> {
            ChangePasswordForm()
        }
    }
}

@Composable
fun ChangePasswordForm(
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val currentPassword = remember { mutableStateOf("") }
        val currentPasswordVisibility = remember { mutableStateOf(false) }
        val currentPasswordError = remember { mutableStateOf("") }
        PasswordTextField(
            text = "Current Password",
            value = currentPassword.value,
            passwordVisible = currentPasswordVisibility.value,
            onValueChange = {
                currentPasswordError.value = ""
                currentPassword.value = it
            },
            onPasswordVisibleChange = {
                currentPasswordVisibility.value = it
            },
            isError = currentPasswordError.value.isNotBlank(),
            supportingText = {
                Text(text = currentPasswordError.value)
            }
        )

        val newPassword = remember { mutableStateOf("") }
        val newPasswordVisibility = remember { mutableStateOf(false) }
        val newPasswordError = remember { mutableStateOf("") }
        PasswordTextField(
            text = "New Password",
            value = newPassword.value,
            passwordVisible = newPasswordVisibility.value,
            onValueChange = {
                newPasswordError.value = ""
                newPassword.value = it
            },
            onPasswordVisibleChange = {
                newPasswordVisibility.value = it
            },
            isError = newPasswordError.value.isNotBlank(),
            supportingText = {
                Text(text = newPasswordError.value)
            }
        )

        val confirmPassword = remember { mutableStateOf("") }
        val confirmPasswordVisibility = remember { mutableStateOf(false) }
        val confirmPasswordError = remember { mutableStateOf("") }
        PasswordTextField(
            text = "Confirm Password",
            value = confirmPassword.value,
            passwordVisible = confirmPasswordVisibility.value,
            onValueChange = {
                confirmPasswordError.value = ""
                confirmPassword.value = it
            },
            onPasswordVisibleChange = {
                confirmPasswordVisibility.value = it
            },
            isError = confirmPasswordError.value.isNotBlank(),
            supportingText = {
                Text(text = confirmPasswordError.value)
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = AppThemeButtonShape,
            onClick = {
                var isError = false
                if (currentPassword.value.isBlank()) {
                    currentPasswordError.value = "Please enter current password"
                    isError = true
                }
                if (newPassword.value.isBlank()) {
                    newPasswordError.value = "Please enter new password"
                    isError = true
                }
                if (confirmPassword.value.isBlank()) {
                    confirmPasswordError.value = "Please enter confirm password"
                    isError = true
                }
                if (confirmPassword.value != newPassword.value) {
                    confirmPasswordError.value = "New password and confirm password does not match"
                    isError = true
                }

                if (!isError) {
                    viewModel.changePassword(currentPassword.value, newPassword.value)
                }
            },
        ) {
            Text(
                text = "Save",
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Preview
@Composable()
fun ChangePasswordScreenPreview() {
    AppTheme {
        ChangePasswordScreen(
            onBackPress = {

            }
        )
    }
}