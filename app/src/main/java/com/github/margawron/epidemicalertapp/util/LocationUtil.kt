package com.github.margawron.epidemicalertapp.util

import android.location.Location
import androidx.lifecycle.Observer
import com.github.margawron.epidemicalertapp.api.location.LocationDto
import com.github.margawron.epidemicalertapp.api.location.LocationType
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

    fun getLocationsLiveDataObserver(googleMap: GoogleMap): Observer<Collection<LocationDto>> = Observer<Collection<LocationDto>> { locations ->
        googleMap.clear()
        locations.forEach{ location ->
            val latLng = LatLng(location.latitude, location.longitude)
            val markerOptions = MarkerOptions()
                .title(getFormattedLatLng(latLng))
                .snippet(location.description)
                .position(latLng)
            when(location.locationType){
                LocationType.INFO -> {
                    markerOptions
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                }
                LocationType.QUARANTINE -> {
                    markerOptions
                        .icon(BitmapDescriptorFactory.defaultMarker(50.0f))
                }
            }
            val marker = googleMap.addMarker(markerOptions)
            marker.tag = location
        }
    }
}