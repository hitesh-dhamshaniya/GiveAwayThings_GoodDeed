package give.away.good.deeds.ui.screens.setting.profile

import android.widget.Toast
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import give.away.good.deeds.R
import give.away.good.deeds.ui.screens.app_common.InfoAlertDialog
import give.away.good.deeds.ui.screens.app_common.SimpleTextFieldView
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
            ProfileForm(
                onBackPress = onBackPress
            )
        }
    }
}

@Composable
fun ProfileForm(
    onBackPress: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {

    val context = LocalContext.current
    if (viewModel.updateSuccess) {
        LaunchedEffect(Unit, block = {
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            onBackPress()
        })
    }

    val showDialog = remember { mutableStateOf(false) }
    if (viewModel.errorMessage.isNotBlank()) {
        LaunchedEffect(Unit, block = {
            showDialog.value = true
        })
    }

    if (showDialog.value)
        InfoAlertDialog(
            title = stringResource(id = R.string.app_name),
            message = viewModel.errorMessage,
            onDismiss = {
                showDialog.value = false
            }
        )

    val user by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit, block = {
        viewModel.fetchUser()
    })

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ProfileImageView(
            imageUri = user.profilePic,
            onAdd = {
                user.profilePic = it
            }
        )

        val fNameError = remember { mutableStateOf("") }
        SimpleTextFieldView(
            text = "First name",
            value = user.firstName ?: "",
            onValueChange = {
                fNameError.value = ""
                user.firstName = it
            },
            isError = fNameError.value.isNotBlank(),
            supportingText = {
                Text(text = fNameError.value)
            }
        )

        val lNameError = remember { mutableStateOf("") }
        SimpleTextFieldView(
            text = "Last name",
            value = user.lastName ?: "",
            onValueChange = {
                lNameError.value = ""
                user.lastName = it
            },
            isError = lNameError.value.isNotBlank(),
            supportingText = {
                Text(text = lNameError.value)
            }
        )

        SimpleTextFieldView(
            text = "Email address",
            value = user.email ?: "",
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
                if (user.firstName.isNullOrBlank()) {
                    fNameError.value = "Please enter first name"
                    isError = true
                }
                if (user.lastName.isNullOrBlank()) {
                    lNameError.value = "Please enter last name"
                    isError = true
                }
                if (!isError) {
                    viewModel.updateProfile(context, user)
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
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            onBackPress = {

            }
        )
    }
}