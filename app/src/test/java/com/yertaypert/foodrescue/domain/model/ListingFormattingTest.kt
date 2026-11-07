package com.yertaypert.foodrescue.domain.model

import com.yertaypert.foodrescue.data.remote.RecipeDto
import com.yertaypert.foodrescue.data.remote.toListing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListingFormattingTest {

    @Test
    fun formattedDistance_formatsImpreciseDecimalsToSingleDecimalPlace() {
        val listing = Listing(
            id = 1,
            title = "Test",
            giverName = "Giver",
            distanceKm = 0.5000000000000001,
            category = Category.Bakery,
            minutesLeft = 30,
            description = "Desc"
        )
        assertEquals("0.5 km", listing.formattedDistance)
    }

    @Test
    fun formattedDistance_formatsWholeNumberWithSingleDecimalPlace() {
        val listing = Listing(
            id = 2,
            title = "Test",
            giverName = "Giver",
            distanceKm = 2.0,
            category = Category.Bakery,
            minutesLeft = 30,
            description = "Desc"
        )
        assertEquals("2.0 km", listing.formattedDistance)
    }

    @Test
    fun recipeMapper_generatesCleanDistanceFormatForRangeOfIds() {
        for (id in 1..30) {
            val dto = RecipeDto(id = id, name = "Recipe $id")
            val listing = dto.toListing()
            assertTrue(
                "Expected single decimal place for id=$id, got ${listing.formattedDistance}",
                listing.formattedDistance.matches(Regex("""^\d+\.\d km$"""))
            )
        }
    }
}
