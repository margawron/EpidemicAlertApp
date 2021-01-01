package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.util.LocationUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.qualifiers.ActivityContext

class MarkPlaceViewModel @ViewModelInject internal constructor(
    private val measurementRepository: MeasurementRepository,
    @ActivityContext private val context: Context
): ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null

    fun mapReadyCallback() = OnMapReadyCallback{
        googleMap = it
        setupInitialZoom()
        setupPlaceSelector()
    }

    private fun setupPlaceSelector() {
        googleMap.setOnMapLongClickListener {
            marker?.remove()
            val markerOptions = MarkerOptions()
                .position(it)
                .title("Wybrana pozycja")
                .snippet(LocationUtil.getFormattedLatLng(it))
            marker = googleMap.addMarker(markerOptions)
        }
    }

    private fun setupInitialZoom(){
        val currentLocation = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?>{ measurement ->
            measurement?.apply {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        measurement.latitude,
                        measurement.longitude
                    ), 12.0f
                )
                googleMap.animateCamera(cameraUpdate)
                currentLocation.removeObservers(lifecycleOwner)
            }
        }
        currentLocation.observe(lifecycleOwner, observer)
    }
}