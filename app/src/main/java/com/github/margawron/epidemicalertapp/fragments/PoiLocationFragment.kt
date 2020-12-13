package com.github.margawron.epidemicalertapp.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.margawron.epidemicalertapp.viewmodels.PoiLocationFragmentViewModel
import com.github.margawron.epidemicalertapp.R

class PoiLocationFragment : Fragment() {

    companion object {
        fun newInstance() = PoiLocationFragment()
    }

    private lateinit var viewModel: PoiLocationFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.poi_location_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PoiLocationFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}