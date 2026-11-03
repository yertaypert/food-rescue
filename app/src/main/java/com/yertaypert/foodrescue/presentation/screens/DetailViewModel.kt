package com.yertaypert.foodrescue.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yertaypert.foodrescue.di.AppContainer
import com.yertaypert.foodrescue.domain.repository.FavouritesRepository
import com.yertaypert.foodrescue.domain.repository.ListingRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "DetailViewModel"

class DetailViewModel(
    private val listingId: Int,
    private val listingRepository: ListingRepository = AppContainer.listingRepository,
    private val favouritesRepository: FavouritesRepository = AppContainer.favouritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var favouriteIds: Set<Int> = emptySet()

    init {
        Log.d(TAG, "init($listingId)")
        loadListing()
        viewModelScope.launch {
            favouritesRepository.favouriteIds.collect { ids ->
                favouriteIds = ids
                (_uiState.value as? DetailUiState.Success)?.let { state ->
                    _uiState.value = state.copy(isFavourite = listingId in ids)
                }
            }
        }
    }

    fun onFavouriteClick() {
        favouritesRepository.toggleFavourite(listingId)
    }

    fun onClaimClick() {
        (_uiState.value as? DetailUiState.Success)?.let { state ->
            _uiState.value = state.copy(isAuthDialogVisible = true)
        }
    }

    fun onAuthDialogDismiss() {
        (_uiState.value as? DetailUiState.Success)?.let { state ->
            _uiState.value = state.copy(isAuthDialogVisible = false)
        }
    }

    fun onRetry() = loadListing()

    private fun loadListing() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val listing = listingRepository.getListing(listingId)
                _uiState.value = if (listing == null) {
                    DetailUiState.NotFound
                } else {
                    DetailUiState.Success(
                        listing = listing,
                        isFavourite = listingId in favouriteIds
                    )
                }
            } catch (e: IOException) {
                _uiState.value = DetailUiState.Error(
                    "No internet connection. Check your connection and try again."
                )
            } catch (e: HttpException) {
                _uiState.value = DetailUiState.Error(
                    "Server error (${e.code()}). Please try again later."
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(
                    "Something went wrong. Please try again."
                )
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
            listingRepository: ListingRepository = AppContainer.listingRepository,
            favouritesRepository: FavouritesRepository = AppContainer.favouritesRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DetailViewModel(listingId, listingRepository, favouritesRepository)
            }
        }
    }
}
