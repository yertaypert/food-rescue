package com.yertaypert.foodrescue.presentation

import androidx.lifecycle.ViewModel
import com.yertaypert.foodrescue.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Small accessor for the persisted Firebase session.
 *
 * This deliberately queries the repository on demand instead of caching the value:
 * on a cold start Firebase may still be restoring the persisted session, so a value
 * captured once during composition can be stale.
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun isSignedIn(): Boolean = authRepository.currentUserId != null
}
