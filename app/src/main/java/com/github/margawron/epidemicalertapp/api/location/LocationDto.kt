package com.github.margawron.epidemicalertapp.api.location

import java.time.Instant

data class LocationDto (
    var id: Long? = null,
    var expiryDate: Instant,
    var latitude: Double,
    var longitude: Double,
    var locationType: LocationType,
    var description: String
)