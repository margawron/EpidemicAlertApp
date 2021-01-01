package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.MarkPlaceActivityBinding
import com.github.margawron.epidemicalertapp.viewmodels.MarkPlaceViewModel
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkPlaceActivity : AppCompatActivity() {

    private val viewModel by viewModels<MarkPlaceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MarkPlaceActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.mark_place_activity)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        viewModel.lifecycleOwner = this

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mark_place_map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(viewModel.mapReadyCallback())
    }
}