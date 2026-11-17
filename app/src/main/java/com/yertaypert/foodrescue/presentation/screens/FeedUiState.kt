package com.yertaypert.foodrescue.presentation.screens

import com.yertaypert.foodrescue.domain.model.Category
import com.yertaypert.foodrescue.domain.model.Listing

sealed interface FeedUiState {
    data object Loading : FeedUiState

    data class Success(
        val listings: List<Listing>,                  // already filtered
        val endingSoon: List<Listing>,                // already sorted
        val selectedCategory: Category?,
        val favouriteIds: Set<Int>
    ) : FeedUiState

    data class Error(val message: String) : FeedUiState
}

/** State of the coarse-location permission and the fix it unlocks. */
sealed interface LocationStatus {
    /** Not determined yet — the system permission dialog may be showing. */
    data object Pending : LocationStatus

    /** Permission granted and a fix was resolved. */
    data class Granted(val displayName: String) : LocationStatus

    /** Permission not granted; the feed keeps working without it. */
    data object Denied : LocationStatus

    /** Permission granted but device-wide location services are turned off. */
    data object ServicesOff : LocationStatus

    /** Permission granted, services on, but no fix could be obtained. */
    data object NoFix : LocationStatus
}
