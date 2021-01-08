package com.github.margawron.epidemicalertapp.api.alerts

data class AlertDto(
    var id: Long,
    var proximityType: ProximityType,
    var victimMeasurements: MutableList<ProximityMeasurementDto>,
    var suspectMeasurements: MutableList<ProximityMeasurementDto>,
)