package com.yertaypert.foodrescue.presentation.screens

import androidx.lifecycle.ViewModel
import com.yertaypert.foodrescue.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileUiState(email = authRepository.currentUserEmail.orEmpty())
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onSignOut() = authRepository.signOut()
}
