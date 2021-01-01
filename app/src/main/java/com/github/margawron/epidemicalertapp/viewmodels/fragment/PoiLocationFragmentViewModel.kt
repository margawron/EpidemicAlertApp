package com.github.margawron.epidemicalertapp.viewmodels.fragment

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.margawron.epidemicalertapp.data.locations.LocationRepository
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.util.LocationUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PoiLocationFragmentViewModel @ViewModelInject internal constructor(
    private val measurementRepository: MeasurementRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    lateinit var context: Context
    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var googleMap: GoogleMap

    fun mapReadyCallback() = OnMapReadyCallback {
        googleMap = it
        zoomOnCurrentLocation()
        setupPoiLocation()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                locationRepository.fetchLocationNearby(context, lifecycleOwner)
            }
        }
    }

    private fun zoomOnCurrentLocation() {
        val currentLocation = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?> { measurement ->
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

    private fun setupPoiLocation(){
        val observer = LocationUtil.getLocationsLiveDataObserver(googleMap)
        locationRepository.getLocationsLiveData().observe(lifecycleOwner, observer)
    }

}