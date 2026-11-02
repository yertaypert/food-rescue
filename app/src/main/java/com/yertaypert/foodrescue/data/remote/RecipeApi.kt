package com.yertaypert.foodrescue.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes")
    suspend fun getRecipes(@Query("limit") limit: Int): RecipesResponse

    @GET("recipes/{id}")
    suspend fun getRecipe(@Path("id") id: Int): RecipeDto
}
