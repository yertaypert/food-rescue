package com.yertaypert.foodrescue.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yertaypert.foodrescue.presentation.components.AuthFooter
import com.yertaypert.foodrescue.presentation.components.AuthSubmitButton
import com.yertaypert.foodrescue.presentation.components.CategorySelector
import com.yertaypert.foodrescue.presentation.components.FormErrorText
import com.yertaypert.foodrescue.presentation.components.FormTextField
import com.yertaypert.foodrescue.presentation.theme.FoodRescueTheme
import com.yertaypert.foodrescue.presentation.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessSignUpScreen(
    uiState: BusinessSignUpUiState,
    onBusinessNameChange: (String) -> Unit,
    onBusinessTypeChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text("Business sign-up") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Column(modifier = Modifier.navigationBarsPadding()) {
                    HorizontalDivider()
                    AuthSubmitButton(
                        text = "Create business account",
                        isLoading = uiState.isSubmitting,
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.md),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            FormTextField(
                label = "Business name",
                value = uiState.businessName,
                onValueChange = onBusinessNameChange,
                placeholder = "Golden Crumb Bakery",
                errorText = uiState.businessNameError,
                imeAction = ImeAction.Next
            )

            CategorySelector(
                label = "Business type",
                options = BUSINESS_TYPES,
                selected = uiState.businessType,
                onSelect = onBusinessTypeChange
            )

            FormTextField(
                label = "Pickup address",
                value = uiState.address,
                onValueChange = onAddressChange,
                placeholder = "14 Baker Street",
                errorText = uiState.addressError,
                imeAction = ImeAction.Next
            )

            FormTextField(
                label = "Email",
                value = uiState.email,
                onValueChange = onEmailChange,
                placeholder = "contact@goldencrumb.com",
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

            AuthFooter(
                prompt = "Already have an account? ",
                actionText = "Log in",
                onActionClick = onLogin
            )
        }
    }
}

@Composable
fun BusinessSignUpRoute(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onAccountCreated: () -> Unit,
    viewModel: BusinessSignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isAccountCreated) {
        if (uiState.isAccountCreated) {
            viewModel.consumeAccountCreated()
            onAccountCreated()
        }
    }

    BusinessSignUpScreen(
        uiState = uiState,
        onBusinessNameChange = viewModel::onBusinessNameChange,
        onBusinessTypeChange = viewModel::onBusinessTypeChange,
        onAddressChange = viewModel::onAddressChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::onSubmit,
        onBack = onBack,
        onLogin = onLogin
    )
}

private fun previewBusinessSignUpScreen(
    state: BusinessSignUpUiState = BusinessSignUpUiState()
): @Composable () -> Unit = {
    FoodRescueTheme {
        BusinessSignUpScreen(
            uiState = state,
            onBusinessNameChange = {},
            onBusinessTypeChange = {},
            onAddressChange = {},
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
private fun BusinessSignUpScreenPreview() = previewBusinessSignUpScreen()()

@Preview(showBackground = true)
@Composable
private fun BusinessSignUpScreenErrorPreview() = previewBusinessSignUpScreen(
    BusinessSignUpUiState(
        businessName = "Golden Crumb Bakery",
        submitError = "An account with this email already exists. Try logging in instead."
    )
)()

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BusinessSignUpScreenDarkPreview() = previewBusinessSignUpScreen()()
