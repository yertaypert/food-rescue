package com.yertaypert.foodrescue.di

import com.yertaypert.foodrescue.data.repository.FavouritesRepositoryImpl
import com.yertaypert.foodrescue.data.repository.ListingRepositoryImpl
import com.yertaypert.foodrescue.domain.repository.FavouritesRepository
import com.yertaypert.foodrescue.domain.repository.ListingRepository

object AppContainer {
    val listingRepository: ListingRepository by lazy { ListingRepositoryImpl.shared }
    val favouritesRepository: FavouritesRepository by lazy { FavouritesRepositoryImpl.shared }
}
