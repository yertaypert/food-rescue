package com.yertaypert.foodrescue.domain.repository

sealed interface AuthResult {
    data object Success : AuthResult
    data class Error(val error: AuthError) : AuthResult
}

enum class AuthError {
    EmailInUse,
    WeakPassword,
    InvalidEmail,
    ProviderDisabled,
    InvalidCredentials,
    UserNotFound,
    UserDisabled,
    TooManyRequests,
    Network,
    Unknown
}

interface AuthRepository {
    val currentUserId: String?
    val currentUserEmail: String?

    suspend fun signUp(email: String, password: String, displayName: String): AuthResult

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun sendPasswordResetEmail(email: String): AuthResult

    fun signOut()
}
