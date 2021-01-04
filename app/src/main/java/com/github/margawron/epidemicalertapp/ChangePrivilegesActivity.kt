package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.margawron.epidemicalertapp.databinding.ChangePriviliagesActivityBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.UserPrivilegesAdapter
import com.github.margawron.epidemicalertapp.databinds.viewmodels.activity.ChangePrivilegesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePrivilegesActivity : AppCompatActivity() {

    private val viewModel by viewModels<ChangePrivilegesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ChangePriviliagesActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.change_priviliages_activity)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        viewModel.binding = binding

        binding.roleRecyclerUsers.also {
            it.setHasFixedSize(true)
            it.adapter = UserPrivilegesAdapter(listOf())
        }
    }
}