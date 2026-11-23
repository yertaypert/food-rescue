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

private const val TAG = "BusinessSignUpViewModel"

@HiltViewModel
class BusinessSignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessSignUpUiState())
    val uiState: StateFlow<BusinessSignUpUiState> = _uiState.asStateFlow()

    fun onBusinessNameChange(value: String) = _uiState.update {
        it.copy(businessName = value, businessNameError = null, submitError = null)
    }

    fun onBusinessTypeChange(value: String) = _uiState.update {
        it.copy(businessType = value)
    }

    fun onAddressChange(value: String) = _uiState.update {
        it.copy(address = value, addressError = null, submitError = null)
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

        val businessName = state.businessName.trim()
        val address = state.address.trim()
        val email = state.email.trim()
        val businessNameError = AuthValidation.nameError(
            businessName,
            emptyMessage = "Enter your business name"
        )
        val addressError = if (address.isBlank()) "Enter the pickup address" else null
        val emailError = AuthValidation.emailError(email)
        val passwordError = AuthValidation.signUpPasswordError(state.password)
        if (businessNameError != null || addressError != null ||
            emailError != null || passwordError != null
        ) {
            _uiState.update {
                it.copy(
                    businessNameError = businessNameError,
                    addressError = addressError,
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, submitError = null) }
            val result = try {
                authRepository.signUp(email, state.password, businessName)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AuthResult.Error(AuthError.Unknown)
            }
            when (result) {
                AuthResult.Success -> {
                    saveProfile(
                        uid = authRepository.currentUserId,
                        email = email,
                        businessName = businessName,
                        businessType = state.businessType,
                        address = address
                    )
                    _uiState.update { it.copy(isSubmitting = false, isAccountCreated = true) }
                }
                is AuthResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, submitError = result.error.toUserMessage())
                }
            }
        }
    }

    fun consumeAccountCreated() = _uiState.update { it.copy(isAccountCreated = false) }

    private suspend fun saveProfile(
        uid: String?,
        email: String,
        businessName: String,
        businessType: String,
        address: String
    ) {
        val userId = uid ?: run {
            Log.w(TAG, "No uid after sign-up; skipping profile save")
            return
        }
        val saved = userProfileRepository.saveProfile(
            UserProfile(
                uid = userId,
                email = email,
                displayName = businessName,
                accountType = AccountType.BUSINESS,
                businessType = businessType,
                address = address
            )
        )
        if (!saved) Log.w(TAG, "Profile save failed for $userId")
    }
}
