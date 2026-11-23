package com.yertaypert.foodrescue.presentation.screens

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val submitError: String? = null,
    val isSubmitting: Boolean = false,
    val isAccountCreated: Boolean = false
)
