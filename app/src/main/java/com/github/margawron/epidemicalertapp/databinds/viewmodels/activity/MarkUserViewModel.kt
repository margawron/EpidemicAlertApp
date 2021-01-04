package com.github.margawron.epidemicalertapp.databinds.viewmodels.activity

import android.content.Context
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.data.users.UserRepository
import com.github.margawron.epidemicalertapp.databinding.MarkUserActivityBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.UserSuspectAdapter
import com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter.UserMarkSuspectViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MarkUserViewModel @ViewModelInject internal constructor(
    private val userRepository: UserRepository,
    @ActivityContext private val context: Context,
) : ViewModel() {

    lateinit var binding: MarkUserActivityBinding
    private var usernameCooldown: Job? = null

    var username = ""
        set(value) {
            field = value
            usernameCooldown?.cancel()
            usernameCooldown = viewModelScope.launch {
                delay(3_000)
                fetchUsers()
            }
        }

    private suspend fun fetchUsers() {
        when(val response = userRepository.findUserByName(username)){
            is ApiResponse.Success -> {
                val users = response.body!!
                val viewModels = users.map {
                    UserMarkSuspectViewModel(
                        it.id!!,
                        it.username,
                        onMarkUserCallback()
                    )
                }
                binding.markUserRecycler.adapter = UserSuspectAdapter(viewModels)
            }
            is ApiResponse.Error -> {
                val errors = ApiResponse.errorToMessage(response)
                Toast.makeText(context, errors, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onMarkUserCallback() = UserMarkSuspectViewModel.OnUserMarkClick{ userId: Long, username: String ->

    }
}