package com.yertaypert.foodrescue.presentation.screens

import com.yertaypert.foodrescue.domain.model.Listing

sealed interface DetailUiState {
    data object Loading : DetailUiState

    data object NotFound : DetailUiState

    data class Success(
        val listing: Listing,
        val isFavourite: Boolean = false,
        val isAuthDialogVisible: Boolean = false
    ) : DetailUiState

    data class Error(val message: String) : DetailUiState
}
