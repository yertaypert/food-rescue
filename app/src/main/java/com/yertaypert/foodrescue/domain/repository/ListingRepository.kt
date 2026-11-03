package com.yertaypert.foodrescue.domain.repository

import com.yertaypert.foodrescue.domain.model.Listing

interface ListingRepository {
    suspend fun getListings(): List<Listing>

    suspend fun getListing(id: Int): Listing?
}
