package com.github.margawron.epidemicalertapp.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.viewmodels.LocationHistoryFragmentViewModel
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.databinding.LocationHistoryFragmentBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class LocationHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = LocationHistoryFragment()
    }

    private lateinit var viewModel: LocationHistoryFragmentViewModel
    private lateinit var binding: LocationHistoryFragmentBinding
    private lateinit var googleMap: GoogleMap

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
        }
    }
}