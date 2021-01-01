package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.RegisterActivityBinding
import com.github.margawron.epidemicalertapp.viewmodels.activity.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: RegisterActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.register_activity)
        binding.lifecycleOwner = this
        binding.registerViewModel = registerViewModel
        registerViewModel.binding = binding
    }

}