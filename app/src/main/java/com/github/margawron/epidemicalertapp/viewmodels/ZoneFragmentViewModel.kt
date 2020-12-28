package com.github.margawron.epidemicalertapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class ZoneFragmentViewModel @ViewModelInject internal constructor(
    private val measurementRepository: MeasurementRepository,
) : ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var googleMap: GoogleMap

    fun mapReadyCallback() = OnMapReadyCallback{
        googleMap = it
        setupZones()
    }

    private fun setupZones(){
        val currentLocation = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?>{ measurement ->
            measurement?.apply {
                showZone(measurement, currentLocation)
            }
        }
        currentLocation.observe(lifecycleOwner, observer)
    }

    private fun showZone(
        measurement: Measurement,
        currentLocation: LiveData<Measurement?>
    ) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(
                measurement.latitude,
                measurement.longitude
            ), 10.0f
        )
        googleMap.animateCamera(cameraUpdate)
        currentLocation.removeObservers(lifecycleOwner)
        // TODO geocode current location
        // Get data from server
    }
}