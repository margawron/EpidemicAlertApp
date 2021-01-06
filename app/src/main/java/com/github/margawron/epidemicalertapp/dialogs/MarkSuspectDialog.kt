package com.github.margawron.epidemicalertapp.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.users.UserDto
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenRepository
import com.github.margawron.epidemicalertapp.databinding.MarkSuspectDialogBinding
import com.github.margawron.epidemicalertapp.databinds.viewmodels.dialogs.MarkSuspectViewModel

class MarkSuspectDialog internal constructor(
    private val pathogenRepository: PathogenRepository,
    private val userDto: UserDto,
    private val onMarkSuspectResult: MarkSuspectViewModel.OnMarkSuspect,
): AppCompatDialogFragment() {

    private val viewModel by lazy {
        MarkSuspectViewModel(pathogenRepository, userDto, onMarkSuspectResult)
    }

    lateinit var binding: MarkSuspectDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding: MarkSuspectDialogBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mark_suspect_dialog, null, false)
        binding.lifecycleOwner = activity
        binding.vm = viewModel
        viewModel.binding = binding
        viewModel.dialog = this
        viewModel.context = requireContext()
        viewModel.setup()

        dialog.setContentView(binding.root)
        return dialog
    }
}