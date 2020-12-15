package com.github.margawron.epidemicalertapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.databinding.LocationHistoryFragmentBinding
import com.github.margawron.epidemicalertapp.viewmodels.LocationHistoryFragmentViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class LocationHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = LocationHistoryFragment()
    }

    private lateinit var viewModel: LocationHistoryFragmentViewModel
    private lateinit var binding: LocationHistoryFragmentBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var polyline: Polyline

    @Inject
    lateinit var measurementRepository: MeasurementRepository

    @Inject
    lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.location_history_fragment, container, false)
        viewModel = ViewModelProvider(this).get(LocationHistoryFragmentViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragment = childFragmentManager.findFragmentById(R.id.mapFragment)
        val mapFragment = fragment as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync {
            googleMap = it
            createPolyline()
        }
    }

    private fun createPolyline() {
        CoroutineScope(Dispatchers.IO).launch {
            val todaysUserLocation = measurementRepository.getLocationsForDay(
                authManager.getLoggedInUser()!!,
                Instant.now()
            )
            val polyLineOptions = with(PolylineOptions()) {
                width(3.0f)
                color(Color.RED)
                geodesic(true)
            }
            todaysUserLocation.forEach { polyLineOptions.add(LatLng(it.latitude, it.longitude)) }
            withContext(Dispatchers.Main) {
                polyline = googleMap.addPolyline(polyLineOptions)

            }
        }
    }
}