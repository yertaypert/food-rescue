package com.yertaypert.foodrescue.data

import kotlinx.coroutines.delay
import java.io.IOException

class ListingRepository(
    var shouldFail: Boolean = false   // set to true to test the error state
) {
    private val favouriteIds = mutableSetOf<Int>()

    suspend fun getListings(): List<Listing> {
        delay(1500)                                   // pretend to be a server
        if (shouldFail) throw IOException("Could not load listings")
        return SampleData.listings
    }

    suspend fun toggleFavourite(id: Int): Set<Int> {
        delay(200)
        if (!favouriteIds.add(id)) favouriteIds.remove(id)
        return favouriteIds.toSet()
    }
}