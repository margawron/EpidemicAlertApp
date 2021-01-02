package com.github.margawron.epidemicalertapp.databinds.viewmodels.activity

import android.content.Context
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.data.users.Role
import com.github.margawron.epidemicalertapp.data.users.UserRepository
import com.github.margawron.epidemicalertapp.databinding.ChangePriviliagesActivityBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.UserAdapter
import com.github.margawron.epidemicalertapp.databinds.viewmodels.items.UserItemViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*

class ChangePrivilegesViewModel @ViewModelInject internal constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    lateinit var binding: ChangePriviliagesActivityBinding

    private var usernameCooldown: Job? = null
    private val roles = Role.values().map {
        context.getString(it.stringId)
    }

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
                    UserItemViewModel(
                        it.id!!,
                        it.username,
                        it.useremail,
                        it.role,
                        onSetRoleCallback(),
                        roles
                    )
                }
                binding.roleRecyclerUsers.adapter = UserAdapter(viewModels)
            }
            is ApiResponse.Error -> {
                val errors = ApiResponse.errorToMessage(response)
                Toast.makeText(context, errors, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSetRoleCallback() = UserItemViewModel.OnSetRoleClickCallback { id: Long, role: Role ->
        viewModelScope.launch{
            when(val response = userRepository.changeUserRole(id, role)){
                is ApiResponse.Success -> {
                    val userDto = response.body!!
                    Toast.makeText(context, "${userDto.username} jest teraz ${userDto.role}", Toast.LENGTH_SHORT).show()
                    fetchUsers()
                }
                is ApiResponse.Error -> {
                    val errors = ApiResponse.errorToMessage(response)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, errors, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}