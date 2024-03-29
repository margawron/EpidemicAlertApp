package com.github.margawron.epidemicalertapp.databinds.viewmodels.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.qualifiers.ActivityContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class LocationHistoryFragmentViewModel @ViewModelInject internal constructor(
    private val authManager: AuthManager,
    private val measurementRepository: MeasurementRepository,
    @ActivityContext private val context: Context
) : ViewModel() {

    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var googleMap: GoogleMap

    private var displayedDate = LocalDate.now()
    private lateinit var selectedDateUserLocation: LiveData<List<Measurement>>
    var isMapTouchedByUser = false

    fun onMapReady() = OnMapReadyCallback { map ->
        googleMap = map
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.setOnCameraMoveStartedListener {
            if(it == REASON_GESTURE){
                isMapTouchedByUser = true
            }
        }
        googleMap.setOnMapLongClickListener {
            isMapTouchedByUser = false
        }
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

    fun mapTouchedByUser() {
        isMapTouchedByUser = true
    }

    private fun setupHistoryLineGenerator() {
        selectedDateUserLocation = measurementRepository.getLocationsForDay(
            authManager.getLoggedInUser(),
            displayedDate
        )
        selectedDateUserLocation.observe(lifecycleOwner){ list ->
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
                    it.measurementTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
                )

                googleMap.addMarker(markerOptions)
            }
            val latLngBounds = LatLngBounds.builder()
            list.takeLast(10).forEach {
                latLngBounds.include(LatLng(it.latitude, it.longitude))
            }
            googleMap.addPolyline(polyLineOptions)
            if (!isMapTouchedByUser) {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 50)
                )
            }
        }
    }
}