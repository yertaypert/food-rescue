package com.yertaypert.foodrescue.domain.location

import com.yertaypert.foodrescue.domain.model.LocationData

/** Result of a location fetch; never throws. */
sealed interface LocationOutcome {
    data class Success(val data: LocationData) : LocationOutcome

    /** Device-wide location services are turned off. */
    data object ServicesOff : LocationOutcome

    /** Services on, but no fix was produced in time. */
    data object NoFix : LocationOutcome
}

interface LocationProvider {
    suspend fun getCurrentLocation(): LocationOutcome
}
