package com.yertaypert.foodrescue.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yertaypert.foodrescue.presentation.components.AuthChoiceDialog
import com.yertaypert.foodrescue.presentation.components.AuthFooter
import com.yertaypert.foodrescue.presentation.components.AuthHeader
import com.yertaypert.foodrescue.presentation.components.AuthSubmitButton
import com.yertaypert.foodrescue.presentation.components.FormErrorText
import com.yertaypert.foodrescue.presentation.components.FormTextField
import com.yertaypert.foodrescue.presentation.theme.FoodRescueTheme
import com.yertaypert.foodrescue.presentation.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onForgotPassword: () -> Unit,
    onShowAuthDialog: () -> Unit,
    onDismissAuthDialog: () -> Unit,
    onRegisterUser: () -> Unit,
    onRegisterGiver: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                value = uiState.email,
                onValueChange = onEmailChange,
                placeholder = "alex@email.com",
                errorText = uiState.emailError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

            FormTextField(
                label = "Password",
                value = uiState.password,
                onValueChange = onPasswordChange,
                isPassword = true,
                errorText = uiState.passwordError,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { onSubmit() })
            )

            TextButton(
                onClick = onForgotPassword,
                enabled = !uiState.isSendingReset && !uiState.isSubmitting,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = if (uiState.isSendingReset) {
                        "Sending reset link…"
                    } else {
                        "Forgot password?"
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (uiState.resetEmailMessage != null) {
                Text(
                    text = uiState.resetEmailMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            FormErrorText(uiState.submitError)

            AuthSubmitButton(
                text = "Log in",
                isLoading = uiState.isSubmitting,
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth()
            )

            AuthFooter(
                prompt = "New here? ",
                actionText = "Create an account",
                onActionClick = onShowAuthDialog
            )
        }
    }

    if (uiState.isAuthDialogVisible) {
        AuthChoiceDialog(
            onDismiss = onDismissAuthDialog,
            onRegisterUser = {
                onDismissAuthDialog()
                onRegisterUser()
            },
            onRegisterGiver = {
                onDismissAuthDialog()
                onRegisterGiver()
            },
            onLogin = onDismissAuthDialog,
            showLoginLink = false
        )
    }
}

@Composable
fun LoginRoute(
    onBack: () -> Unit,
    onRegisterUser: () -> Unit,
    onRegisterGiver: () -> Unit,
    onSignedIn: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            viewModel.consumeSignInSuccess()
            onSignedIn()
        }
    }

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::onSubmit,
        onBack = onBack,
        onForgotPassword = viewModel::onForgotPassword,
        onShowAuthDialog = viewModel::onShowAuthDialog,
        onDismissAuthDialog = viewModel::onDismissAuthDialog,
        onRegisterUser = onRegisterUser,
        onRegisterGiver = onRegisterGiver
    )
}

private fun previewLoginScreen(state: LoginUiState = LoginUiState()): @Composable () -> Unit = {
    FoodRescueTheme {
        LoginScreen(
            uiState = state,
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onBack = {},
            onForgotPassword = {},
            onShowAuthDialog = {},
            onDismissAuthDialog = {},
            onRegisterUser = {},
            onRegisterGiver = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() = previewLoginScreen()()

@Preview(showBackground = true)
@Composable
private fun LoginScreenErrorPreview() =
    previewLoginScreen(LoginUiState(submitError = "Incorrect email or password."))()

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenDarkPreview() = previewLoginScreen()()
