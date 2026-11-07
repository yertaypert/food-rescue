package com.yertaypert.foodrescue.data.remote

import com.yertaypert.foodrescue.domain.model.Category
import com.yertaypert.foodrescue.domain.model.Listing

fun RecipeDto.toListing(): Listing {
    val cuisineName = cuisine.orEmpty().trim()
    val prep = prepTimeMinutes ?: 0
    val cook = cookTimeMinutes ?: 0
    val totalMinutes = prep + cook

    return Listing(
        id = id,
        title = name.orEmpty().ifBlank { "Untitled recipe" },
        giverName = if (cuisineName.isBlank()) "DummyJSON Kitchen" else "$cuisineName Kitchen",
        distanceKm = ((id % 15 + 1) + 3) / 10.0,
        category = toCategory(),
        minutesLeft = totalMinutes.coerceAtLeast(1),
        description = instructions
            ?.joinToString(" ")
            ?.trim()
            .orEmpty()
            .ifBlank { "Recipe from DummyJSON." },
        pickupWindow = if (totalMinutes > 0) "$prep-$totalMinutes min" else "See recipe",
        area = cuisineName.ifBlank { "DummyJSON" }
    )
}

private fun RecipeDto.toCategory(): Category {
    val text = buildString {
        append(name.orEmpty())
        append(' ')
        append(cuisine.orEmpty())
        append(' ')
        append(tags.orEmpty().joinToString(" "))
        append(' ')
        append(mealType.orEmpty().joinToString(" "))
    }.lowercase()

    return when {
        listOf("dessert", "baking", "cake", "cookie", "bread", "pastry", "pie")
            .any { text.contains(it) } -> Category.Bakery

        listOf("drink", "beverage", "smoothie", "juice", "coffee", "tea")
            .any { text.contains(it) } -> Category.Drinks

        listOf("dairy", "cheese", "milk", "yogurt")
            .any { text.contains(it) } -> Category.Dairy

        listOf("vegetarian", "vegan", "salad", "vegetable", "produce", "fruit")
            .any { text.contains(it) } -> Category.Produce

        else -> Category.Meals
    }
}
