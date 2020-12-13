package com.github.margawron.epidemicalertapp.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.viewmodels.ZoneFragmentViewModel

class ZoneFragment : Fragment() {

    companion object {
        fun newInstance() = ZoneFragment()
    }

    private lateinit var viewModel: ZoneFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.zone_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ZoneFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}