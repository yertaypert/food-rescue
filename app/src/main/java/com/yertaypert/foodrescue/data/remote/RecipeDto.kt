package com.yertaypert.foodrescue.data.remote

data class RecipeDto(
    val id: Int = 0,
    val name: String? = null,
    val instructions: List<String>? = null,
    val prepTimeMinutes: Int? = null,
    val cookTimeMinutes: Int? = null,
    val cuisine: String? = null,
    val tags: List<String>? = null,
    val mealType: List<String>? = null
)

data class RecipesResponse(
    val recipes: List<RecipeDto>? = null
)
