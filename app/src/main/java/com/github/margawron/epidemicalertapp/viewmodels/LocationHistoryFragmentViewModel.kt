package com.github.margawron.epidemicalertapp.viewmodels

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.qualifiers.ActivityContext
import java.time.LocalDate
import java.time.ZoneId

class LocationHistoryFragmentViewModel @ViewModelInject internal constructor(
    private val authManager: AuthManager,
    private val measurementRepository: MeasurementRepository,
    @ActivityContext private val context: Context
) : ViewModel() {

    lateinit var googleMap: GoogleMap

    private var displayedDate = LocalDate.now()
    private lateinit var selectedDateUserLocation: LiveData<List<Measurement>>
    var isMapTouchedByUser = false

    fun refresh(){
        setupHistoryLineGenerator()
    }

    fun changeDisplayedDate() {
        val listener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                displayedDate = LocalDate.of(year, month + 1, dayOfMonth)
                setupHistoryLineGenerator()
            }
        val pickerDialog = DatePickerDialog(
            context,
            listener,
            displayedDate.year,
            displayedDate.monthValue - 1,
            displayedDate.dayOfMonth
        )
        pickerDialog.show()
    }

    fun mapTouchedByUser(){
        isMapTouchedByUser = true
    }

    private fun setupHistoryLineGenerator() {
        selectedDateUserLocation = measurementRepository.getLocationsForDay(
            authManager.getLoggedInUser(),
            displayedDate
        )
        selectedDateUserLocation.observeForever { list ->
            val polyLineOptions = with(PolylineOptions()) {
                width(3.0f)
                color(Color.RED)
                geodesic(true)
            }
            googleMap.clear()
            setupMap(list, polyLineOptions)
        }
    }


    private fun setupMap(
        list: List<Measurement>,
        polyLineOptions: PolylineOptions
    ) {
        if (list.isNotEmpty()) {
            val mapBoundsBuilder = LatLngBounds.Builder()
            list.forEach {
                val latLng = LatLng(it.latitude, it.longitude)

                polyLineOptions.add(latLng)

                mapBoundsBuilder.include(latLng)

                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title(
                    it.measurementTime.atZone(ZoneId.systemDefault()).toString()
                )

                googleMap.addMarker(markerOptions)
            }
            val latLngBounds = LatLngBounds.builder()
            list.takeLast(10).forEach {
                latLngBounds.include(LatLng(it.latitude, it.longitude))
            }
            googleMap.addPolyline(polyLineOptions)
            val latestLL = list.last()
            if (!isMapTouchedByUser) {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(latestLL.latitude, latestLL.longitude),
                        16.0f
                    )
                )
            }
        }
    }
}