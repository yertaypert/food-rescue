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
