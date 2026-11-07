package com.yertaypert.foodrescue.domain.model

import java.util.Locale


data class Listing(
    val id: Int,
    val title: String,
    val giverName: String,
    val distanceKm: Double,
    val category: Category,
    val minutesLeft: Int,
    val description: String,
    val pickupWindow: String = "6:00 - 7:30 PM",
    val area: String = "Baker Street area"
) {
    val formattedDistance: String
        get() = String.format(Locale.US, "%.1f km", distanceKm)
}
