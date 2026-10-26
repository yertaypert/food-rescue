package com.yertaypert.foodrescue.ui.screens

import com.yertaypert.foodrescue.data.Listing

data class DetailUiState(
    val isLoading: Boolean = true,
    val listing: Listing? = null,
    val error: String? = null,
    val isFavourite: Boolean = false,
    val isAuthDialogVisible: Boolean = false
)
