package com.github.margawron.epidemicalertapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.databinding.LocationHistoryFragmentBinding
import com.github.margawron.epidemicalertapp.viewmodels.LocationHistoryFragmentViewModel
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = LocationHistoryFragment()
    }

    private val viewModel by viewModels<LocationHistoryFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: LocationHistoryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.location_history_fragment, container, false)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        val fragment = childFragmentManager.findFragmentById(R.id.locationHistoryMapFragment)
        val mapFragment = fragment as SupportMapFragment
        mapFragment.getMapAsync(viewModel.onMapReady())
        return binding.root
    }
}