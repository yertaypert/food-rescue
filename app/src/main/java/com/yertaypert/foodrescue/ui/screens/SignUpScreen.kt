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
import androidx.compose.material.icons.outlined.RamenDining
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.yertaypert.foodrescue.ui.components.AuthHeader
import com.yertaypert.foodrescue.ui.components.FormTextField
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing

private const val MIN_PASSWORD_LENGTH = 6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onSubmit: (name: String, email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun submit() {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()

        nameError = if (trimmedName.isEmpty()) "Enter your name" else null
        emailError = when {
            trimmedEmail.isEmpty() -> "Enter your email"
            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Enter a valid email"
            else -> null
        }
        passwordError = when {
            password.isEmpty() -> "Enter a password"
            password.length < MIN_PASSWORD_LENGTH ->
                "Use at least $MIN_PASSWORD_LENGTH characters"
            else -> null
        }

        if (nameError == null && emailError == null && passwordError == null) {
            onSubmit(trimmedName, trimmedEmail, password)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Create your account") },
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
                icon = Icons.Outlined.RamenDining,
                subtitle = "Find and claim surplus food nearby.",
                horizontalAlignment = Alignment.Start
            )

            FormTextField(
                label = "Name",
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                placeholder = "Aidana Serik",
                errorText = nameError,
                imeAction = ImeAction.Next
            )

            FormTextField(
                label = "Email",
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                placeholder = "aidana@email.com",
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

            Button(
                onClick = { submit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.sm)
            ) {
                Text("Create account")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.sm),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Log in",
                    modifier = Modifier.clickable(onClick = onLogin),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    FoodRescueTheme { SignUpScreen(onBack = {}, onSubmit = { _, _, _ -> }) }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignUpScreenDarkPreview() {
    FoodRescueTheme { SignUpScreen(onBack = {}, onSubmit = { _, _, _ -> }) }
}