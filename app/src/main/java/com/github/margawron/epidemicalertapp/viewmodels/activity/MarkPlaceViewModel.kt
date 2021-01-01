package com.github.margawron.epidemicalertapp.viewmodels.activity

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.api.location.LocationType
import com.github.margawron.epidemicalertapp.dialogs.AddLocationDialog
import com.github.margawron.epidemicalertapp.util.LocationUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.qualifiers.ActivityContext
import java.time.Instant

class MarkPlaceViewModel @ViewModelInject internal constructor(
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
                Toast.makeText(
                    appCompatActivity,
                    "$description $expiryTime $locationType",
                    Toast.LENGTH_SHORT
                ).show()
                val latLng = marker!!.position

            }.show(appCompatActivity.supportFragmentManager, "add location dialog")
        }
    }

    fun removeLocation() {
        // get id from tag
        // markerToRemove?.tag
    }

    private fun setupMarkerRemoval() {
        googleMap.setOnMarkerClickListener {
            marker?.remove()
            marker = null
            markerToRemove = it
            true
        }
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