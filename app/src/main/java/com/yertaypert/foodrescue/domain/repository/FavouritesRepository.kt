package com.yertaypert.foodrescue.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface FavouritesRepository {
    val favouriteIds: StateFlow<Set<Int>>

    fun toggleFavourite(id: Int)
}
