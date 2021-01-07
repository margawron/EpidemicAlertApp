package com.github.margawron.epidemicalertapp.databinds.viewmodels.activity

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.suspects.SuspectDto
import com.github.margawron.epidemicalertapp.api.suspects.SuspectService
import com.github.margawron.epidemicalertapp.api.users.UserDto
import com.github.margawron.epidemicalertapp.data.alerts.SuspicionLevel
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenRepository
import com.github.margawron.epidemicalertapp.data.users.UserRepository
import com.github.margawron.epidemicalertapp.databinding.MarkUserActivityBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.UserSuspectAdapter
import com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter.UserMarkSuspectViewModel
import com.github.margawron.epidemicalertapp.dialogs.MarkSuspectDialog
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

class MarkUserViewModel @ViewModelInject internal constructor(
    private val suspectService: SuspectService,
    private val pathogenRepository: PathogenRepository,
    private val userRepository: UserRepository,
    @ActivityContext private val context: Context,
) : ViewModel() {

    lateinit var binding: MarkUserActivityBinding
    lateinit var activity: AppCompatActivity
    private var usernameCooldown: Job? = null

    var users = listOf<UserDto>()
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
                users = response.body!!
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
        val userDto = users.find { it.id!! == userId }!!
        MarkSuspectDialog(pathogenRepository, userDto){ pathogenInfectionTime: Instant, suspicionLevel: SuspicionLevel, suspectId: Long, pathogenId: Long ->
            viewModelScope.launch {
                when(val response = suspectService.reportSuspect(SuspectDto(null, pathogenInfectionTime, suspicionLevel, suspectId, pathogenId))){
                    is ApiResponse.Success -> {
                        Toast.makeText(
                            context,
                            "${context.getString(R.string.mark_user_success)} $username",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ApiResponse.Error -> {
                        val error = ApiResponse.errorToMessage(response)
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }.show(activity.supportFragmentManager, "mark user dialog")
    }
}