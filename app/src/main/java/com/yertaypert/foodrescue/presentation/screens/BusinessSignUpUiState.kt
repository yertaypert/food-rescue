package com.yertaypert.foodrescue.presentation.screens

val BUSINESS_TYPES = listOf(
    "Restaurant",
    "Café",
    "Bakery",
    "Grocery store",
    "Convenience store",
    "Other"
)

data class BusinessSignUpUiState(
    val businessName: String = "",
    val businessType: String = BUSINESS_TYPES.first(),
    val address: String = "",
    val email: String = "",
    val password: String = "",
    val businessNameError: String? = null,
    val addressError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val submitError: String? = null,
    val isSubmitting: Boolean = false,
    val isAccountCreated: Boolean = false
)
