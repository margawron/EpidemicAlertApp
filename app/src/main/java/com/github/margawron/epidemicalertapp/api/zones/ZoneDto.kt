package com.github.margawron.epidemicalertapp.api.zones

import java.time.Instant

data class ZoneDto(
    var id: Long? = null,
    val name: String,
    val zoneEmergencyLevel: ZoneEmergencyLevel,
    val activeSince: Instant
)