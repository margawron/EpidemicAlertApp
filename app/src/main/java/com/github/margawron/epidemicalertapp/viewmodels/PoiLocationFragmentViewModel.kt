package com.github.margawron.epidemicalertapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class PoiLocationFragmentViewModel @ViewModelInject internal constructor(
    private val measurementRepository: MeasurementRepository,
) : ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var googleMap: GoogleMap

    fun mapReadyCallback() = OnMapReadyCallback{
        googleMap = it
        showPointsOfInterests()
    }

    private fun showPointsOfInterests(){
        val currentLocation = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?>{ measurement ->
            measurement?.apply {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        measurement.latitude,
                        measurement.longitude
                    ), 10.0f
                )
                googleMap.animateCamera(cameraUpdate)
                currentLocation.removeObservers(lifecycleOwner)
            }
        }
        currentLocation.observe(lifecycleOwner, observer)
    }

}