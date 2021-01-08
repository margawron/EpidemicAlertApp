package com.github.margawron.epidemicalertapp.api.alerts

import java.time.Instant

data class ProximityMeasurementDto(
    var id: Long,
    var timestamp: Instant,
    var latitude: Double,
    var longitude: Double,
)