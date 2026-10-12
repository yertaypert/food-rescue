package com.yertaypert.foodrescue.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isPassword: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val trailing: (@Composable () -> Unit)? = if (isPassword) {
        {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff
                    else Icons.Outlined.Visibility,
                    contentDescription = if (passwordVisible) "Hide password"
                    else "Show password"
                )
            }
        }
    } else null

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let {
                { Text(text = it, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            },
            singleLine = true,
            isError = errorText != null,
            supportingText = errorText?.let {
                { Text(text = it, maxLines = 2, overflow = TextOverflow.Ellipsis) }
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = trailing,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormTextFieldPreview() {
    FoodRescueTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            FormTextField(
                label = "Email",
                value = "",
                onValueChange = {},
                placeholder = "alex@email.com"
            )
            FormTextField(
                label = "Password",
                value = "secret123",
                onValueChange = {},
                isPassword = true,
                errorText = "Enter your password"
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FormTextFieldDarkPreview() {
    FoodRescueTheme {
        FormTextField(
            label = "Email",
            value = "alex@email.com",
            onValueChange = {}
        )
    }
}