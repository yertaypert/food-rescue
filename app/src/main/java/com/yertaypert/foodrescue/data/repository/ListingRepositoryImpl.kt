package com.yertaypert.foodrescue.data.repository

import com.yertaypert.foodrescue.data.remote.RecipeApi
import com.yertaypert.foodrescue.data.remote.RetrofitClient
import com.yertaypert.foodrescue.data.remote.toListing
import com.yertaypert.foodrescue.domain.model.Listing
import com.yertaypert.foodrescue.domain.repository.ListingRepository
import java.io.IOException

class ListingRepositoryImpl(
    private val api: RecipeApi = RetrofitClient.recipeApi,
    var shouldFail: Boolean = false   // set to true to test the error state
) : ListingRepository {

    override suspend fun getListings(): List<Listing> {
        if (shouldFail) throw IOException("Could not load listings")
        return api.getRecipes(limit = 20).recipes.orEmpty().map { it.toListing() }
    }

    override suspend fun getListing(id: Int): Listing? {
        if (shouldFail) throw IOException("Could not load listing")
        return api.getRecipe(id).toListing()
    }

    companion object {
        val shared = ListingRepositoryImpl()
    }
}
