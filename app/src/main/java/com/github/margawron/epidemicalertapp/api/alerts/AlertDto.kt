package com.github.margawron.epidemicalertapp.api.alerts

import com.github.margawron.epidemicalertapp.data.alerts.SuspicionLevel

data class AlertDto(
    var id: Long,
    var proximityType: ProximityType,
    var suspicionLevel: SuspicionLevel,
    var pathogenId: Long,
    var victimId: Long,
    var victimMeasurements: List<ProximityMeasurementDto>,
    var suspectId: Long,
    var suspectMeasurements: List<ProximityMeasurementDto>,
)