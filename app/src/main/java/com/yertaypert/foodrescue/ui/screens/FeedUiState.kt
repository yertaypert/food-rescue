package com.yertaypert.foodrescue.ui.screens

import com.yertaypert.foodrescue.data.Category
import com.yertaypert.foodrescue.data.Listing

data class FeedUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedCategory: Category? = null,
    val listings: List<Listing> = emptyList(),    // already filtered
    val endingSoon: List<Listing> = emptyList(),  // already sorted
    val favouriteIds: Set<Int> = emptySet()
)