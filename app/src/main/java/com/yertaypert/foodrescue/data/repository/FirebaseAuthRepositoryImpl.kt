package com.yertaypert.foodrescue.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.yertaypert.foodrescue.domain.repository.AuthError
import com.yertaypert.foodrescue.domain.repository.AuthRepository
import com.yertaypert.foodrescue.domain.repository.AuthResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirebaseAuthRepository"

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override val currentUserEmail: String?
        get() = firebaseAuth.currentUser?.email

    override suspend fun signUp(
        email: String,
        password: String,
        displayName: String
    ): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val name = displayName.trim()
            val user = result.user
            if (name.isNotEmpty() && user != null) {
                try {
                    user.updateProfile(
                        UserProfileChangeRequest.Builder().setDisplayName(name).build()
                    ).await()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // The account already exists; a missing display name is not fatal.
                    Log.w(TAG, "Could not set display name for ${user.uid}", e)
                }
            }
            AuthResult.Success
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthException) {
            Log.w(TAG, "Sign-up failed: ${e.errorCode}", e)
            AuthResult.Error(e.toAuthError())
        } catch (e: Exception) {
            Log.w(TAG, "Sign-up failed", e)
            AuthResult.Error(AuthError.Unknown)
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthException) {
            Log.w(TAG, "Sign-in failed: ${e.errorCode}", e)
            AuthResult.Error(e.toSignInError())
        } catch (e: Exception) {
            Log.w(TAG, "Sign-in failed", e)
            AuthResult.Error(AuthError.Unknown)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthResult.Success
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseAuthException) {
            Log.w(TAG, "Password reset failed: ${e.errorCode}", e)
            val error = if (e.errorCode == "ERROR_USER_NOT_FOUND") {
                AuthError.UserNotFound
            } else {
                e.toAuthError()
            }
            AuthResult.Error(error)
        } catch (e: Exception) {
            Log.w(TAG, "Password reset failed", e)
            AuthResult.Error(AuthError.Unknown)
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    private fun FirebaseAuthException.toAuthError(): AuthError = when (errorCode) {
        "ERROR_EMAIL_ALREADY_IN_USE",
        "ERROR_CREDENTIAL_ALREADY_IN_USE" -> AuthError.EmailInUse
        "ERROR_WEAK_PASSWORD" -> AuthError.WeakPassword
        "ERROR_INVALID_EMAIL" -> AuthError.InvalidEmail
        "ERROR_OPERATION_NOT_ALLOWED" -> AuthError.ProviderDisabled
        "ERROR_INVALID_CREDENTIAL" -> AuthError.InvalidCredentials
        "ERROR_USER_NOT_FOUND" -> AuthError.UserNotFound
        "ERROR_USER_DISABLED" -> AuthError.UserDisabled
        "ERROR_QUOTA_EXCEEDED",
        "ERROR_TOO_MANY_REQUESTS" -> AuthError.TooManyRequests
        "ERROR_NETWORK_REQUEST_FAILED" -> AuthError.Network
        else -> AuthError.Unknown
    }

    private fun FirebaseAuthException.toSignInError(): AuthError = when (this) {
        // With email-enumeration protection on (the default for new projects), Firebase
        // reports a wrong password as ERROR_INVALID_CREDENTIAL, so match by type first.
        is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
        is FirebaseAuthInvalidUserException ->
            if (errorCode == "ERROR_USER_DISABLED") {
                AuthError.UserDisabled
            } else {
                AuthError.InvalidCredentials
            }
        else -> when (errorCode) {
            "ERROR_INVALID_CREDENTIAL",
            "ERROR_WRONG_PASSWORD",
            "ERROR_USER_NOT_FOUND" -> AuthError.InvalidCredentials
            else -> toAuthError()
        }
    }
}
