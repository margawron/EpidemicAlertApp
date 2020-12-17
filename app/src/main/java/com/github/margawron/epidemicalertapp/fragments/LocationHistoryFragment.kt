package com.github.margawron.epidemicalertapp.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.databinding.LocationHistoryFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class LocationHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = LocationHistoryFragment()
    }

    private lateinit var googleMap: GoogleMap
    private var polyline: Polyline? = null
    private var isMapTouchedByUser: Boolean = false
    private var displayedDate = LocalDate.now()
    private lateinit var selectedDateUserLocation: LiveData<List<Measurement>>

    @Inject
    lateinit var measurementRepository: MeasurementRepository

    @Inject
    lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: LocationHistoryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.location_history_fragment, container, false)
        binding.lifecycleOwner = this
        val fragment = childFragmentManager.findFragmentById(R.id.locationHistoryMapFragment)
        val mapFragment = fragment as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync {
            googleMap = it
            setupHistoryLineGenerator()
        }
        binding.root.setOnClickListener { isMapTouchedByUser = true }
        binding.locationFragmentFab.setOnClickListener {
            val listener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    displayedDate = LocalDate.of(year, month+1, dayOfMonth)
                    setupHistoryLineGenerator()
                }
            val pickerDialog = DatePickerDialog(requireContext(),listener, displayedDate.year, displayedDate.monthValue-1, displayedDate.dayOfMonth)
            pickerDialog.show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isMapTouchedByUser = false
    }

    private fun setupHistoryLineGenerator() {
        selectedDateUserLocation = measurementRepository.getLocationsForDay(
            authManager.getLoggedInUser()!!,
            displayedDate
        )
        selectedDateUserLocation.observe(viewLifecycleOwner) { list ->
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
            polyline = googleMap.addPolyline(polyLineOptions)
            val latestLL = list.last()
            if (!isMapTouchedByUser) {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(latestLL.latitude, latestLL.longitude),
                        14.0f
                    )
                )
            }
        }
    }
}