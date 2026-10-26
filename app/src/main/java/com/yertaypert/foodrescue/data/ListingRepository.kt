package com.yertaypert.foodrescue.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

class ListingRepository(
    var shouldFail: Boolean = false   // set to true to test the error state
) {
    private val _favouriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favouriteIds: StateFlow<Set<Int>> = _favouriteIds.asStateFlow()

    suspend fun getListings(): List<Listing> {
        if (shouldFail) throw IOException("Could not load listings")
        return SampleData.listings
    }

    suspend fun getListing(id: Int): Listing? {
        if (shouldFail) throw IOException("Could not load listing")
        return SampleData.byId(id)
    }

    fun toggleFavourite(id: Int) {
        _favouriteIds.update { ids ->
            if (id in ids) ids - id else ids + id
        }
    }

    companion object {
        val shared = ListingRepository()
    }
}
