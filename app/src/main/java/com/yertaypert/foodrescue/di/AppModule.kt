package com.yertaypert.foodrescue.di

import com.yertaypert.foodrescue.data.repository.FavouritesRepositoryImpl
import com.yertaypert.foodrescue.data.repository.ListingRepositoryImpl
import com.yertaypert.foodrescue.domain.repository.FavouritesRepository
import com.yertaypert.foodrescue.domain.repository.ListingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideListingRepository(): ListingRepository = ListingRepositoryImpl.shared

    @Provides
    @Singleton
    fun provideFavouritesRepository(): FavouritesRepository = FavouritesRepositoryImpl.shared
}
