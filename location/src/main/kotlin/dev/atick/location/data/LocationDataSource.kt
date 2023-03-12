package dev.atick.location.data

import dev.atick.location.data.models.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    val currentLocation: Flow<Location>
    suspend fun getLastKnownLocation(): Location?
}