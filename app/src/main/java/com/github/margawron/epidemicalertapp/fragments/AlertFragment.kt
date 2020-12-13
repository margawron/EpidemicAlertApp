package com.github.margawron.epidemicalertapp.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.margawron.epidemicalertapp.viewmodels.AlertFragmentViewModel
import com.github.margawron.epidemicalertapp.R

class AlertFragment : Fragment() {

    companion object {
        fun newInstance() = AlertFragment()
    }

    private lateinit var viewModel: AlertFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alert_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlertFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}