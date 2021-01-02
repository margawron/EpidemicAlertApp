package com.github.margawron.epidemicalertapp.databinds.viewmodels.activity

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.location.LocationDto
import com.github.margawron.epidemicalertapp.api.location.LocationType
import com.github.margawron.epidemicalertapp.data.locations.LocationRepository
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.dialogs.AddLocationDialog
import com.github.margawron.epidemicalertapp.util.LocationUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant

class MarkPlaceViewModel @ViewModelInject internal constructor(
    private val locationRepository: LocationRepository,
    private val measurementRepository: MeasurementRepository,
    @ActivityContext private val context: Context
) : ViewModel() {

    lateinit var appCompatActivity: AppCompatActivity
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null
    private var markerToRemove: Marker? = null

    fun mapReadyCallback() = OnMapReadyCallback {
        googleMap = it
        setupInitialZoom()
        setupPlaceSelector()
        setupMarkerRemoval()
        setupLocationsListener()
    }

    fun addLocation() {
        if (marker == null) {
            Toast.makeText(
                appCompatActivity,
                context.getString(R.string.location_not_selected),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            AddLocationDialog() { description: String, expiryTime: Instant, locationType: LocationType ->
                val latLng = marker!!.position
                viewModelScope.launch {
                    when (val response = locationRepository.createLocation(
                        latLng,
                        description,
                        expiryTime,
                        locationType
                    )) {
                        is ApiResponse.Success -> {
                            Toast.makeText(
                                appCompatActivity,
                                context.getString(R.string.location_added),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ApiResponse.Error -> {
                            val error = ApiResponse.errorToMessage(response)
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.show(appCompatActivity.supportFragmentManager, "add location dialog")
        }
    }

    fun removeLocation() {
        if (markerToRemove != null) {
            val locationDto = markerToRemove!!.tag as LocationDto
            val locationId = locationDto.id!!
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    when (val response = locationRepository.removeLocationById(locationId)) {
                        is ApiResponse.Success -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.location_delete_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ApiResponse.Error -> {
                            val error = ApiResponse.errorToMessage(response)
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        } else {
            Toast.makeText(
                appCompatActivity,
                context.getString(R.string.location_to_delete_not_selected),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupMarkerRemoval() {
        googleMap.setOnMarkerClickListener {
            marker?.remove()
            marker = null
            markerToRemove = it
            false
        }
    }

    private fun setupLocationsListener() {
        val observer = LocationUtil.getLocationsLiveDataObserver(googleMap)
        locationRepository.getLocationsLiveData().observe(appCompatActivity, observer)
    }


    private fun setupPlaceSelector() {
        googleMap.setOnMapLongClickListener {
            marker?.remove()
            val markerOptions = MarkerOptions()
                .position(it)
                .title(context.getString(R.string.selected_position))
                .snippet(LocationUtil.getFormattedLatLng(it))
            marker = googleMap.addMarker(markerOptions)
        }
    }

    private fun setupInitialZoom() {
        val currentLocation = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?> { measurement ->
            measurement?.apply {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        measurement.latitude,
                        measurement.longitude
                    ), 15.0f
                )
                googleMap.animateCamera(cameraUpdate)
                currentLocation.removeObservers(appCompatActivity)
            }
        }
        currentLocation.observe(appCompatActivity, observer)
    }
}