package com.github.margawron.epidemicalertapp.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.margawron.epidemicalertapp.viewmodels.LocationHistoryFragmentViewModel
import com.github.margawron.epidemicalertapp.R

class LocationHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = LocationHistoryFragment()
    }

    private lateinit var viewModel: LocationHistoryFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.location_history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationHistoryFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}