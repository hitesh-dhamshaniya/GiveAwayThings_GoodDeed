package give.away.good.deeds.ui.screens.app_common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import give.away.good.deeds.R

@Composable
fun SimpleTextFieldView(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
) {
    Column {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = if(isEnabled) Color.Unspecified else Color.LightGray
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            enabled = isEnabled,
            isError = isError,
            supportingText = supportingText,
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun PasswordTextField(
    text: String,
    value: String,
    passwordVisible: Boolean,
    onValueChange: (String) -> Unit,
    onPasswordVisibleChange: (Boolean) -> Unit,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
) {
    Column {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError,
            supportingText = supportingText,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
            trailingIcon = {
                val image = if (passwordVisible) R.drawable.ic_visibility
                else R.drawable.ic_visibility_off

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = {
                    onPasswordVisibleChange(!passwordVisible)
                }) {
                    Image(
                        painter = painterResource(image),
                        contentDescription = description
                    )
                }
            }
        )
    }
}