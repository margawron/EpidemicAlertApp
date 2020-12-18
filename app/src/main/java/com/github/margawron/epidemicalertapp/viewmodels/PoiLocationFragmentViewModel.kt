package com.github.margawron.epidemicalertapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class PoiLocationFragmentViewModel @ViewModelInject internal constructor(
    private val measurementRepository: MeasurementRepository
)
    : ViewModel() {

    private val currentLocation = measurementRepository.getCurrentLocation()
    lateinit var googleMap: GoogleMap

    fun refresh(){
        showPointsOfInterests()
    }

    private fun showPointsOfInterests(){
        currentLocation.observeForever { location ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude
                ), 12.0f
            )
            googleMap.animateCamera(cameraUpdate)
        }
    }
}