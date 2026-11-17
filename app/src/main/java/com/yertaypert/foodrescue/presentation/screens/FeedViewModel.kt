package com.yertaypert.foodrescue.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yertaypert.foodrescue.domain.location.LocationOutcome
import com.yertaypert.foodrescue.domain.location.LocationProvider
import com.yertaypert.foodrescue.domain.model.Category
import com.yertaypert.foodrescue.domain.model.Listing
import com.yertaypert.foodrescue.domain.repository.FavouritesRepository
import com.yertaypert.foodrescue.domain.repository.ListingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "FeedViewModel"

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val listingRepository: ListingRepository,
    private val favouritesRepository: FavouritesRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Loading)
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _locationStatus = MutableStateFlow<LocationStatus>(LocationStatus.Pending)
    val locationStatus: StateFlow<LocationStatus> = _locationStatus.asStateFlow()

    private var allListings: List<Listing> = emptyList()
    private var selectedCategory: Category? = null
    private var favouriteIds: Set<Int> = emptySet()

    init {
        Log.d(TAG, "init")
        loadListings()
        viewModelScope.launch {
            favouritesRepository.favouriteIds.collect { ids ->
                favouriteIds = ids
                (_uiState.value as? FeedUiState.Success)?.let { state ->
                    _uiState.value = state.copy(favouriteIds = ids)
                }
            }
        }
    }

    fun onCategorySelected(category: Category) {
        selectedCategory = if (selectedCategory == category) null else category
        (_uiState.value as? FeedUiState.Success)?.let {
            _uiState.value = successState()
        }
    }

    fun onFavouriteClick(id: Int) {
        favouritesRepository.toggleFavourite(id)
    }

    fun onRetry() = loadListings()

    fun onLocationPermissionGranted() {
        viewModelScope.launch {
            val outcome = try {
                locationProvider.getCurrentLocation()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                LocationOutcome.NoFix
            }
            _locationStatus.value = when (outcome) {
                is LocationOutcome.Success -> LocationStatus.Granted(outcome.data.displayName)
                LocationOutcome.ServicesOff -> LocationStatus.ServicesOff
                LocationOutcome.NoFix -> LocationStatus.NoFix
            }
        }
    }

    fun onLocationPermissionDenied() {
        _locationStatus.value = LocationStatus.Denied
    }

    fun onRetryLocation() = onLocationPermissionGranted()

    private fun loadListings() {
        viewModelScope.launch {
            _uiState.value = FeedUiState.Loading
            try {
                allListings = listingRepository.getListings()
                _uiState.value = successState()
            } catch (e: IOException) {
                _uiState.value = FeedUiState.Error(
                    "No internet connection. Check your connection and try again."
                )
            } catch (e: HttpException) {
                _uiState.value = FeedUiState.Error(
                    "Server error (${e.code()}). Please try again later."
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = FeedUiState.Error(
                    "Something went wrong. Please try again."
                )
            }
        }
    }

    private fun successState(): FeedUiState.Success {
        val filtered = allListings.filter {
            selectedCategory == null || it.category == selectedCategory
        }
        return FeedUiState.Success(
            listings = filtered,
            endingSoon = filtered.sortedBy { it.minutesLeft }.take(5),
            selectedCategory = selectedCategory,
            favouriteIds = favouriteIds
        )
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }
}
