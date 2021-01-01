package com.github.margawron.epidemicalertapp.util

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

object LocationUtil {

    private val hourMinuteSecondDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    fun getFormattedLocation(location: Location): String{
        val lat = if (location.latitude > 0) "N" else "S"
        val long = if (location.latitude > 0) "E" else "W"
        val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(location.time), ZoneId.systemDefault())
            .format(hourMinuteSecondDateFormatter)
        return "$time ${abs(location.latitude)}°$lat ${abs(location.longitude)}°$long"
    }

    fun getFormattedLatLng(location: LatLng): String{
        val lat = if (location.latitude > 0) "N" else "S"
        val long = if (location.latitude > 0) "E" else "W"
        return "${abs(location.latitude)}°$lat ${abs(location.longitude)}°$long"
    }
}