package com.github.margawron.epidemicalertapp.databinds.viewmodels.dialogs

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.api.alerts.AlertDto
import com.github.margawron.epidemicalertapp.api.alerts.ProximityMeasurementDto
import com.github.margawron.epidemicalertapp.dialogs.AlertDetailsDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AlertDetailsViewModel constructor(
    private val alertDto: AlertDto,
): ViewModel() {

    lateinit var dialog: AlertDetailsDialog
    lateinit var context: Context
    lateinit var googleMap: GoogleMap

    lateinit var suspectPolyline: Polyline
    lateinit var victimPolyline: Polyline

    fun onMapReady() = OnMapReadyCallback { map ->
        googleMap = map
        map.uiSettings.isMapToolbarEnabled = false
        val latLngBounds = LatLngBounds.builder()
        processSuspectMeasurements(alertDto.suspectMeasurements, latLngBounds)
        processVictimMeasurements(alertDto.victimMeasurements, latLngBounds)
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            withContext(Dispatchers.Main){
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 50))
            }

        }
    }

    private fun processSuspectMeasurements(measurements: List<ProximityMeasurementDto>, latLngBounds: LatLngBounds.Builder) {
        val polylineOptions = PolylineOptions()
        with(polylineOptions){
            color(Color.RED)
            width(3.0f)
            geodesic(true)
        }
        measurements.forEach {
            val latLng = LatLng(it.latitude, it.longitude)
            polylineOptions.add(LatLng(it.latitude,it.longitude))
            latLngBounds.include(latLng)

            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            markerOptions.position(latLng)
            markerOptions.title(it.timestamp.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_TIME))
            googleMap.addMarker(markerOptions)
        }
        suspectPolyline = googleMap.addPolyline(polylineOptions)
    }

    private fun processVictimMeasurements(measurements: List<ProximityMeasurementDto>, latLngBounds: LatLngBounds.Builder) {
        val polylineOptions = PolylineOptions()
        with(polylineOptions){
            color(Color.GREEN)
            width(3.0f)
            geodesic(true)
        }
        measurements.forEach {
            val latLng = LatLng(it.latitude, it.longitude)
            polylineOptions.add(latLng)
            latLngBounds.include(latLng)

            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            markerOptions.position(latLng)
            markerOptions.title(it.timestamp.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_TIME))
            googleMap.addMarker(markerOptions)
        }
        victimPolyline = googleMap.addPolyline(polylineOptions)
    }
}