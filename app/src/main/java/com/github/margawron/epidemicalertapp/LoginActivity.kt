package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.LoginActivityBinding
import com.github.margawron.epidemicalertapp.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: LoginActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.login_activity)
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModel
    }
}