package com.yertaypert.foodrescue.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yertaypert.foodrescue.domain.repository.AuthError
import com.yertaypert.foodrescue.domain.repository.AuthRepository
import com.yertaypert.foodrescue.domain.repository.AuthResult
import com.yertaypert.foodrescue.presentation.util.AuthValidation
import com.yertaypert.foodrescue.presentation.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) = _uiState.update {
        it.copy(
            email = value,
            emailError = null,
            submitError = null,
            resetEmailMessage = null
        )
    }

    fun onPasswordChange(value: String) = _uiState.update {
        it.copy(password = value, passwordError = null, submitError = null)
    }

    fun onShowAuthDialog() = _uiState.update { it.copy(isAuthDialogVisible = true) }

    fun onDismissAuthDialog() = _uiState.update { it.copy(isAuthDialogVisible = false) }

    fun onSubmit() {
        val state = _uiState.value
        if (state.isSubmitting) return

        val email = state.email.trim()
        val emailError = AuthValidation.emailError(email)
        val passwordError = AuthValidation.signInPasswordError(state.password)
        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    submitError = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submitError = null) }
            val result = try {
                authRepository.signIn(email, state.password)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AuthResult.Error(AuthError.Unknown)
            }
            _uiState.update {
                when (result) {
                    AuthResult.Success -> it.copy(
                        isSubmitting = false,
                        isSignedIn = true
                    )
                    is AuthResult.Error -> it.copy(
                        isSubmitting = false,
                        submitError = result.error.toUserMessage()
                    )
                }
            }
        }
    }

    fun onForgotPassword() {
        val state = _uiState.value
        if (state.isSendingReset) return

        val email = state.email.trim()
        val emailError = AuthValidation.emailError(email)
        if (emailError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    submitError = null,
                    resetEmailMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSendingReset = true, submitError = null, resetEmailMessage = null)
            }
            val result = try {
                authRepository.sendPasswordResetEmail(email)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AuthResult.Error(AuthError.Unknown)
            }
            // Never reveal whether the address is registered.
            val confirmation = "If an account exists for $email, a reset link is on its way."
            _uiState.update { current ->
                when (result) {
                    AuthResult.Success -> current.copy(
                        isSendingReset = false,
                        resetEmailMessage = confirmation
                    )
                    is AuthResult.Error -> if (result.error == AuthError.UserNotFound) {
                        current.copy(isSendingReset = false, resetEmailMessage = confirmation)
                    } else {
                        current.copy(
                            isSendingReset = false,
                            submitError = result.error.toUserMessage()
                        )
                    }
                }
            }
        }
    }

    fun consumeSignInSuccess() = _uiState.update { it.copy(isSignedIn = false) }
}
