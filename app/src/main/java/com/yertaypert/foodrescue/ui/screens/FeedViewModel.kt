package com.yertaypert.foodrescue.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yertaypert.foodrescue.data.Category
import com.yertaypert.foodrescue.data.Listing
import com.yertaypert.foodrescue.data.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "FeedViewModel"

class FeedViewModel(
    private val repository: ListingRepository = ListingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private var allListings: List<Listing> = emptyList()

    init {
        Log.d(TAG, "init")
        loadListings()
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { state ->
            val newCategory = if (state.selectedCategory == category) null else category
            applyFilter(state.copy(selectedCategory = newCategory))
        }
    }

    fun onFavouriteClick(id: Int) {
        viewModelScope.launch {
            val ids = repository.toggleFavourite(id)
            _uiState.update { it.copy(favouriteIds = ids) }
        }
    }

    fun onRetry() = loadListings()

    private fun loadListings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                allListings = repository.getListings()
                _uiState.update { applyFilter(it.copy(isLoading = false)) }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Something went wrong")
                }
            }
        }
    }

    private fun applyFilter(state: FeedUiState): FeedUiState {
        val filtered = allListings.filter {
            state.selectedCategory == null || it.category == state.selectedCategory
        }
        return state.copy(
            listings = filtered,
            endingSoon = filtered.sortedBy { it.minutesLeft }.take(5)
        )
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }
}