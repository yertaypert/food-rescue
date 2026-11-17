package com.yertaypert.foodrescue.data.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.yertaypert.foodrescue.domain.location.LocationOutcome
import com.yertaypert.foodrescue.domain.location.LocationProvider
import com.yertaypert.foodrescue.domain.model.LocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.math.abs

/** Max time to wait for a location fix before reporting NoFix. */
private const val FIX_TIMEOUT_MS = 5_000L

/**
 * Coarse location provider backed by the Play Services fused provider.
 * Falls back to a device geocoder for a readable name, then to raw coordinates.
 */
@Singleton
class AndroidLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    private val fusedClient by lazy { LocationServices.getFusedLocationProviderClient(context) }
    private val geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    override suspend fun getCurrentLocation(): LocationOutcome = withContext(Dispatchers.Default) {
        if (!isLocationEnabled()) return@withContext LocationOutcome.ServicesOff

        val location = try {
            withTimeout(FIX_TIMEOUT_MS) {
                lastKnownLocation() ?: requestFreshLocation()
            }
        } catch (e: TimeoutCancellationException) {
            null
        } ?: return@withContext LocationOutcome.NoFix

        val name = resolveDisplayName(location.latitude, location.longitude)
            ?: formatCoordinates(location.latitude, location.longitude)
        LocationOutcome.Success(
            LocationData(
                latitude = location.latitude,
                longitude = location.longitude,
                displayName = name
            )
        )
    }

    private fun isLocationEnabled(): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(manager)
    }

    private suspend fun lastKnownLocation(): Location? = try {
        fusedClient.lastLocation.awaitOrNull()
    } catch (e: SecurityException) {
        null
    }

    private suspend fun requestFreshLocation(): Location? = try {
        fusedClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        ).awaitOrNull()
    } catch (e: SecurityException) {
        null
    } catch (e: ApiException) {
        null
    }

    @Suppress("DEPRECATION")
    private fun resolveDisplayName(latitude: Double, longitude: Double): String? {
        if (!Geocoder.isPresent()) return null
        return try {
            val address = geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
                ?: return null
            val city = address.locality?.takeIf { it.isNotBlank() }
                ?: address.adminArea?.takeIf { it.isNotBlank() }
            val district = address.subLocality?.takeIf { it.isNotBlank() }
            when {
                city != null && district != null -> "$city, $district"
                city != null -> city
                district != null -> district
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun formatCoordinates(latitude: Double, longitude: Double): String {
        val latDirection = if (latitude >= 0) "N" else "S"
        val lonDirection = if (longitude >= 0) "E" else "W"
        return String.format(
            Locale.US,
            "%.2f°%s, %.2f°%s",
            abs(latitude), latDirection, abs(longitude), lonDirection
        )
    }
}

/** Awaits a Play Services [Task]; resolves to null on failure or cancellation instead of throwing. */
private suspend fun <T> Task<T>.awaitOrNull(): T? = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result ->
        if (continuation.isActive) continuation.resume(result)
    }
    addOnFailureListener {
        if (continuation.isActive) continuation.resume(null)
    }
    addOnCanceledListener {
        if (continuation.isActive) continuation.resume(null)
    }
}
