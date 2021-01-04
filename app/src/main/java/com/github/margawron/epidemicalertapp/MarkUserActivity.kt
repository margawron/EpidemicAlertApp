package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.MarkUserActivityBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.UserSuspectAdapter
import com.github.margawron.epidemicalertapp.databinds.viewmodels.activity.MarkUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkUserActivity: AppCompatActivity() {

    private val viewModel by viewModels<MarkUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MarkUserActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.mark_user_activity)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        viewModel.binding = binding

        binding.markUserRecycler.also {
            it.setHasFixedSize(true)
            it.adapter = UserSuspectAdapter(listOf())
        }
    }
}