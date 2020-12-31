package com.github.margawron.epidemicalertapp.api.measurements

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import java.time.Instant

class MeasurementDto(
    @field:JsonProperty(value = "id")
    val id: Long,
    val measurementTime: Instant,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val bearing: Float,
    val bearingAccuracy: Float,
) {
    companion object{
        fun fromEntity(measurement: Measurement) =
            MeasurementDto(
                measurement.id!!,
                measurement.measurementTime,
                measurement.latitude,
                measurement.longitude,
                measurement.accuracy,
                measurement.bearing,
                measurement.bearingAccuracy
            )
    }
}
