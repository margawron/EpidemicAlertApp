package com.github.margawron.epidemicalertapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.github.margawron.epidemicalertapp.databinds.viewmodels.fragment.AlertFragmentViewModel
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.databinding.AlertFragmentBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.AlertItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertFragment : Fragment() {

    companion object {
        fun newInstance() = AlertFragment()
    }

    private val viewModel by viewModels<AlertFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: AlertFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.alert_fragment, container, false)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        viewModel.binding = binding
        viewModel.onInit()

        binding.alertRecycler.also {
            it.setHasFixedSize(false)
            it.adapter = AlertItemAdapter(listOf())
        }

        return binding.root
    }
}