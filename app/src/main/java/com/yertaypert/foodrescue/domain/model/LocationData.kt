package com.yertaypert.foodrescue.domain.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    /** Human readable name, e.g. "Almaty, Bostandyk district" or "51.17°N, 71.45°E". */
    val displayName: String
)
