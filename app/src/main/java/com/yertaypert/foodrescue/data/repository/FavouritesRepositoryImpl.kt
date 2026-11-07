package com.yertaypert.foodrescue.data.repository

import com.yertaypert.foodrescue.domain.repository.FavouritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesRepositoryImpl @Inject constructor() : FavouritesRepository {
    private val _favouriteIds = MutableStateFlow<Set<Int>>(emptySet())
    override val favouriteIds: StateFlow<Set<Int>> = _favouriteIds.asStateFlow()

    override fun toggleFavourite(id: Int) {
        _favouriteIds.update { ids ->
            if (id in ids) ids - id else ids + id
        }
    }
}
