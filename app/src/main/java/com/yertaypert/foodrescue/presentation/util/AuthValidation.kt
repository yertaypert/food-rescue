package com.yertaypert.foodrescue.presentation.util

import android.util.Patterns

object AuthValidation {
    const val MIN_PASSWORD_LENGTH = 6

    fun nameError(name: String, emptyMessage: String = "Enter your name"): String? =
        if (name.isBlank()) emptyMessage else null

    fun emailError(email: String): String? = when {
        email.isBlank() -> "Enter your email"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Enter a valid email"
        else -> null
    }

    fun signUpPasswordError(password: String): String? = when {
        password.isEmpty() -> "Enter a password"
        password.length < MIN_PASSWORD_LENGTH ->
            "Use at least $MIN_PASSWORD_LENGTH characters"
        else -> null
    }

    fun signInPasswordError(password: String): String? =
        if (password.isEmpty()) "Enter your password" else null
}
