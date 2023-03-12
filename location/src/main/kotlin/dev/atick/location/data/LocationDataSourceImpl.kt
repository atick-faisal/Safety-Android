package dev.atick.location.data

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.*
import dev.atick.location.data.config.Config
import dev.atick.location.data.models.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationDataSourceImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationDataSource {

    override val currentLocation: Flow<Location>
        @SuppressLint("MissingPermission")
        get() = callbackFlow {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, Config.UPDATE_INTERVAL
            ).setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(Config.MIN_UPDATE_INTERVAL)
                .setMaxUpdateDelayMillis(Config.MAX_UPDATE_DELAY)
                .setDurationMillis(Config.UPDATE_DURATION)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    location?.let {
                        trySend(Location(location.latitude, location.longitude))
                    }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): Location? {
        return suspendCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    continuation.resume(
                        Location(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                } ?: continuation.resume(null)
            }
        }
    }
}