package com.yertaypert.foodrescue.ui.screens

import android.content.res.Configuration
import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.yertaypert.foodrescue.ui.components.AuthChoiceDialog
import com.yertaypert.foodrescue.ui.components.AuthHeader
import com.yertaypert.foodrescue.ui.components.FormTextField
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSubmit: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
    onForgotPassword: () -> Unit = {},
    onRegisterUser: () -> Unit = {},
    onRegisterGiver: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showAuthDialog by remember { mutableStateOf(false) }

    fun submit() {
        val trimmedEmail = email.trim()
        emailError = when {
            trimmedEmail.isEmpty() -> "Enter your email"
            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Enter a valid email"
            else -> null
        }
        passwordError = if (password.isEmpty()) "Enter your password" else null

        if (emailError == null && passwordError == null) {
            onSubmit(trimmedEmail, password)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Log in") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            AuthHeader(
                icon = Icons.Outlined.Eco,
                subtitle = "Log in to claim or manage listings",
                modifier = Modifier.padding(vertical = Spacing.md)
            )

            FormTextField(
                label = "Email",
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                placeholder = "alex@email.com",
                errorText = emailError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

            FormTextField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                isPassword = true,
                errorText = passwordError,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { submit() })
            )

            TextButton(
                onClick = onForgotPassword,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Button(
                onClick = { submit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log in")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.sm),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New here? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Create an account",
                    modifier = Modifier.clickable { showAuthDialog = true },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (showAuthDialog) {
        AuthChoiceDialog(
            onDismiss = { showAuthDialog = false },
            onRegisterUser = { showAuthDialog = false; onRegisterUser() },
            onRegisterGiver = { showAuthDialog = false; onRegisterGiver() },
            onLogin = { showAuthDialog = false },
            showLoginLink = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    FoodRescueTheme { LoginScreen(onBack = {}, onSubmit = { _, _ -> }) }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenDarkPreview() {
    FoodRescueTheme { LoginScreen(onBack = {}, onSubmit = { _, _ -> }) }
}