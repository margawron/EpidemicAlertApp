package com.github.margawron.epidemicalertapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.databinding.PoiLocationFragmentBinding
import com.github.margawron.epidemicalertapp.viewmodels.PoiLocationFragmentViewModel
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoiLocationFragment : Fragment() {

    companion object {
        fun newInstance() = PoiLocationFragment()
    }

    private val viewModel by viewModels<PoiLocationFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: PoiLocationFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.poi_location_fragment, container, false)
        binding.lifecycleOwner = this
        val fragment = childFragmentManager.findFragmentById(R.id.locationPoiMapFragment)
        val mapFragment = fragment as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync {
            viewModel.googleMap = it
            viewModel.refresh()
        }

        return binding.root
    }
}