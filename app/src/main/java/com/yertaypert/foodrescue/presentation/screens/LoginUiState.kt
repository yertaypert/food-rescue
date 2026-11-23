package com.yertaypert.foodrescue.presentation.screens

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val submitError: String? = null,
    val resetEmailMessage: String? = null,
    val isSubmitting: Boolean = false,
    val isSendingReset: Boolean = false,
    val isAuthDialogVisible: Boolean = false,
    val isSignedIn: Boolean = false
)
