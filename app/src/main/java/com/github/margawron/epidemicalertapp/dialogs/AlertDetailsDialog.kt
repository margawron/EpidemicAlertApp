package com.github.margawron.epidemicalertapp.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.alerts.AlertDto
import com.github.margawron.epidemicalertapp.databinding.AlertDetailsDialogBinding
import com.github.margawron.epidemicalertapp.databinds.viewmodels.dialogs.AlertDetailsViewModel
import com.google.android.gms.maps.SupportMapFragment

class AlertDetailsDialog internal constructor(
    private val alertDto: AlertDto
): AppCompatDialogFragment()  {

    companion object{
        const val DIALOG_TAG = "show alert details"
    }

    val viewModel: AlertDetailsViewModel by lazy{
        AlertDetailsViewModel(alertDto)
    }

    lateinit var binding: AlertDetailsDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding: AlertDetailsDialogBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.alert_details_dialog, null, false)
        binding.lifecycleOwner = activity
        binding.vm = viewModel
        viewModel.dialog = this
        viewModel.context = requireContext()

        val fragment = childFragmentManager.findFragmentById(R.id.alert_details_map)
        val mapFragment = fragment as SupportMapFragment
        mapFragment.getMapAsync(viewModel.onMapReady())

        dialog.setContentView(binding.root)
        return dialog
    }
}