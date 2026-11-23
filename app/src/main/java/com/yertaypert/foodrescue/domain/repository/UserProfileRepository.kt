package com.yertaypert.foodrescue.domain.repository

import com.yertaypert.foodrescue.domain.model.UserProfile

interface UserProfileRepository {
    /**
     * Persists the profile document for [profile].
     * Returns false when the write could not be performed; the caller decides whether it is fatal.
     */
    suspend fun saveProfile(profile: UserProfile): Boolean
}
