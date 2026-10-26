package com.yertaypert.foodrescue.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yertaypert.foodrescue.data.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "DetailViewModel"

class DetailViewModel(
    private val listingId: Int,
    private val repository: ListingRepository = ListingRepository.shared
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "init($listingId)")
        loadListing()
        viewModelScope.launch {
            repository.favouriteIds.collect { ids ->
                _uiState.update { it.copy(isFavourite = listingId in ids) }
            }
        }
    }

    fun onFavouriteClick() {
        repository.toggleFavourite(listingId)
    }

    fun onClaimClick() {
        _uiState.update { it.copy(isAuthDialogVisible = true) }
    }

    fun onAuthDialogDismiss() {
        _uiState.update { it.copy(isAuthDialogVisible = false) }
    }

    fun onRetry() = loadListing()

    private fun loadListing() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val listing = repository.getListing(listingId)
                _uiState.update {
                    it.copy(isLoading = false, listing = listing, error = null)
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Something went wrong")
                }
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    companion object {
        fun factory(
            listingId: Int,
            repository: ListingRepository = ListingRepository.shared
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { DetailViewModel(listingId, repository) }
        }
    }
}
