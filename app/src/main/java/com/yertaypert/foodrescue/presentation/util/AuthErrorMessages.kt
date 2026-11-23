package com.yertaypert.foodrescue.presentation.util

import com.yertaypert.foodrescue.domain.repository.AuthError

fun AuthError.toUserMessage(): String = when (this) {
    AuthError.EmailInUse ->
        "An account with this email already exists. Try logging in instead."
    AuthError.WeakPassword ->
        "That password is too weak. Use at least 6 characters."
    AuthError.InvalidEmail ->
        "Enter a valid email address."
    AuthError.ProviderDisabled ->
        "Email/password sign-in is not enabled for this Firebase project."
    AuthError.InvalidCredentials ->
        "Incorrect email or password."
    AuthError.UserNotFound ->
        "No account was found for that email."
    AuthError.UserDisabled ->
        "This account has been disabled."
    AuthError.TooManyRequests ->
        "Too many attempts. Wait a moment and try again."
    AuthError.Network ->
        "No internet connection. Check your connection and try again."
    AuthError.Unknown ->
        "Something went wrong. Please try again."
}
