package com.github.margawron.epidemicalertapp.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.databinding.AddLocationAlertBinding
import com.github.margawron.epidemicalertapp.viewmodels.dialogs.AddLocationViewModel

class AddLocationDialog internal constructor(
    private val onAddLocationResult: AddLocationViewModel.OnAddLocationResult
): AppCompatDialogFragment() {

    private val viewModel by lazy {
        AddLocationViewModel(onAddLocationResult)
    }

    lateinit var binding: AddLocationAlertBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding: AddLocationAlertBinding = DataBindingUtil.inflate(layoutInflater, R.layout.add_location_alert, null, false)
        binding.lifecycleOwner = activity
        binding.vm = viewModel
        viewModel.dialog = this
        viewModel.context = requireContext()

        dialog.setContentView(binding.root)
        return dialog
    }
}