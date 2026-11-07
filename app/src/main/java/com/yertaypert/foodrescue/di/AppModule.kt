package com.yertaypert.foodrescue.di

import com.yertaypert.foodrescue.data.repository.FavouritesRepositoryImpl
import com.yertaypert.foodrescue.data.repository.ListingRepositoryImpl
import com.yertaypert.foodrescue.domain.repository.FavouritesRepository
import com.yertaypert.foodrescue.domain.repository.ListingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindListingRepository(
        impl: ListingRepositoryImpl
    ): ListingRepository

    @Binds
    @Singleton
    abstract fun bindFavouritesRepository(
        impl: FavouritesRepositoryImpl
    ): FavouritesRepository
}
