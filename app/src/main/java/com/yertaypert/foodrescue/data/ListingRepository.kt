package com.yertaypert.foodrescue.data

import com.yertaypert.foodrescue.data.remote.RecipeApi
import com.yertaypert.foodrescue.data.remote.RetrofitClient
import com.yertaypert.foodrescue.data.remote.toListing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

class ListingRepository(
    private val api: RecipeApi = RetrofitClient.recipeApi,
    var shouldFail: Boolean = false   // set to true to test the error state
) {
    private val _favouriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favouriteIds: StateFlow<Set<Int>> = _favouriteIds.asStateFlow()

    suspend fun getListings(): List<Listing> {
        if (shouldFail) throw IOException("Could not load listings")
        return api.getRecipes(limit = 20).recipes.orEmpty().map { it.toListing() }
    }

    suspend fun getListing(id: Int): Listing? {
        if (shouldFail) throw IOException("Could not load listing")
        return api.getRecipe(id).toListing()
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
