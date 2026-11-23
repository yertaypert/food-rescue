package com.yertaypert.foodrescue.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yertaypert.foodrescue.domain.model.AccountType
import com.yertaypert.foodrescue.domain.model.UserProfile
import com.yertaypert.foodrescue.domain.repository.AuthError
import com.yertaypert.foodrescue.domain.repository.AuthRepository
import com.yertaypert.foodrescue.domain.repository.AuthResult
import com.yertaypert.foodrescue.domain.repository.UserProfileRepository
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

private const val TAG = "SignUpViewModel"

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update {
        it.copy(name = value, nameError = null, submitError = null)
    }

    fun onEmailChange(value: String) = _uiState.update {
        it.copy(email = value, emailError = null, submitError = null)
    }

    fun onPasswordChange(value: String) = _uiState.update {
        it.copy(password = value, passwordError = null, submitError = null)
    }

    fun onSubmit() {
        val state = _uiState.value
        if (state.isSubmitting) return

        val name = state.name.trim()
        val email = state.email.trim()
        val nameError = AuthValidation.nameError(name)
        val emailError = AuthValidation.emailError(email)
        val passwordError = AuthValidation.signUpPasswordError(state.password)
        if (nameError != null || emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submitError = null) }
            val result = try {
                authRepository.signUp(email, state.password, name)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AuthResult.Error(AuthError.Unknown)
            }
            when (result) {
                AuthResult.Success -> {
                    saveProfile(uid = authRepository.currentUserId, email = email, name = name)
                    _uiState.update { it.copy(isSubmitting = false, isAccountCreated = true) }
                }
                is AuthResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, submitError = result.error.toUserMessage())
                }
            }
        }
    }

    fun consumeAccountCreated() = _uiState.update { it.copy(isAccountCreated = false) }

    private suspend fun saveProfile(uid: String?, email: String, name: String) {
        val userId = uid ?: run {
            Log.w(TAG, "No uid after sign-up; skipping profile save")
            return
        }
        val saved = userProfileRepository.saveProfile(
            UserProfile(
                uid = userId,
                email = email,
                displayName = name,
                accountType = AccountType.USER
            )
        )
        if (!saved) Log.w(TAG, "Profile save failed for $userId")
    }
}
