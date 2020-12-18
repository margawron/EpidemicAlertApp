package com.github.margawron.epidemicalertapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class PoiLocationFragmentViewModel @ViewModelInject internal constructor(
    private val measurementRepository: MeasurementRepository
): ViewModel() {
    lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: LiveData<Measurement?>

    fun refresh(){
        showPointsOfInterests()
    }

    private fun showPointsOfInterests(){
        currentLocation = measurementRepository.getLastLocation()
        currentLocation.observeForever{ location ->
            location?.apply {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 10.0f)
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }
}