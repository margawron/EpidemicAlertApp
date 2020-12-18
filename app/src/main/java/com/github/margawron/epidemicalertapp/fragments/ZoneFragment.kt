package com.github.margawron.epidemicalertapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.databinding.ZoneFragmentBinding
import com.github.margawron.epidemicalertapp.viewmodels.ZoneFragmentViewModel
import com.google.android.gms.maps.SupportMapFragment

class ZoneFragment : Fragment() {

    companion object {
        fun newInstance() = ZoneFragment()
    }

    private val viewModel by viewModels<ZoneFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ZoneFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.zone_fragment, container, false)
        binding.lifecycleOwner = this
        val fragment = childFragmentManager.findFragmentById(R.id.locationZoneMapFragment)
        val mapFragment = fragment as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync {
            viewModel.googleMap = it
            viewModel.refresh()
        }

        return binding.root
    }
}
