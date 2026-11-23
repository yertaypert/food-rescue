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
import androidx.compose.material.icons.outlined.RamenDining
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.yertaypert.foodrescue.presentation.components.AuthFooter
import com.yertaypert.foodrescue.presentation.components.AuthHeader
import com.yertaypert.foodrescue.presentation.components.AuthSubmitButton
import com.yertaypert.foodrescue.presentation.components.FormErrorText
import com.yertaypert.foodrescue.presentation.components.FormTextField
import com.yertaypert.foodrescue.presentation.theme.FoodRescueTheme
import com.yertaypert.foodrescue.presentation.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                value = uiState.name,
                onValueChange = onNameChange,
                placeholder = "Aidana Serik",
                errorText = uiState.nameError,
                imeAction = ImeAction.Next
            )

            FormTextField(
                label = "Email",
                value = uiState.email,
                onValueChange = onEmailChange,
                placeholder = "aidana@email.com",
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

            FormErrorText(uiState.submitError)

            AuthSubmitButton(
                text = "Create account",
                isLoading = uiState.isSubmitting,
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.sm)
            )

            AuthFooter(
                prompt = "Already have an account? ",
                actionText = "Log in",
                onActionClick = onLogin
            )
        }
    }
}

@Composable
fun SignUpRoute(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onAccountCreated: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isAccountCreated) {
        if (uiState.isAccountCreated) {
            viewModel.consumeAccountCreated()
            onAccountCreated()
        }
    }

    SignUpScreen(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::onSubmit,
        onBack = onBack,
        onLogin = onLogin
    )
}

private fun previewSignUpScreen(state: SignUpUiState = SignUpUiState()): @Composable () -> Unit = {
    FoodRescueTheme {
        SignUpScreen(
            uiState = state,
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onBack = {},
            onLogin = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() = previewSignUpScreen()()

@Preview(showBackground = true)
@Composable
private fun SignUpScreenErrorPreview() = previewSignUpScreen(
    SignUpUiState(
        name = "Aidana Serik",
        email = "aidana@email.com",
        submitError = "An account with this email already exists. Try logging in instead."
    )
)()

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignUpScreenDarkPreview() = previewSignUpScreen()()
