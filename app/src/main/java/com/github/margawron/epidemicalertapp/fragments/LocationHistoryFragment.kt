package com.github.margawron.epidemicalertapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
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

    @Inject
    lateinit var measurementRepository: MeasurementRepository

    @Inject
    lateinit var authManager: AuthManager

    // TODO add bar for changing the selected day
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
        binding.root.setOnClickListener{ isMapTouchedByUser = true }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isMapTouchedByUser = false
    }

    private fun setupHistoryLineGenerator() {
        CoroutineScope(Dispatchers.IO).launch {
            val todaysUserLocation = measurementRepository.getLocationsForDay(
                authManager.getLoggedInUser()!!,
                Instant.now()
            )
            withContext(Dispatchers.Main){
                todaysUserLocation.observe(viewLifecycleOwner) { list ->
                    val polyLineOptions = with(PolylineOptions()) {
                        width(3.0f)
                        color(Color.RED)
                        geodesic(true)
                    }
                    setupMap(list, polyLineOptions)
                }
            }
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
            polyline?.remove()
            polyline = googleMap.addPolyline(polyLineOptions)
            val latestLL = list.last()
            if(!isMapTouchedByUser) {
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