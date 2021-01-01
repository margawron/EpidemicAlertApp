package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.SettingsActivityBinding
import com.github.margawron.epidemicalertapp.viewmodels.activity.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: SettingsActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.settings_activity)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        viewModel.activity = this
    }
}