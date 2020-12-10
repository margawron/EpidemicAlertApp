package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.auth.RegisterRequest
import com.github.margawron.epidemicalertapp.databinding.RegisterActivityBinding
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel @ViewModelInject internal constructor(
    private val authManager: AuthManager,
    @ActivityContext val activityContext: Context
) : ViewModel() {
    var binding: RegisterActivityBinding? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var repeatPassword: String = ""

    fun register() {
        binding?.registerButtonRegister?.isClickable = false
        when {
            username.length <= 3 -> {
                binding?.registerInputUsername?.error =
                    "Nazwa użytkownika musi mieć co najmniej 4 znaki"
            }
            password.length <= 4 -> {
                binding?.registerInputPassword?.error = "Hasło musi mieć co najmniej 5 znaków"
            }
            password != repeatPassword -> {
                binding?.registerInputPassword?.error = "Hasła muszą się zgadzać"
                binding?.registerInputRepeatPassword?.error = "Hasła muszą się zgadzać"
            }
            else -> {
                viewModelScope.launch {
                    val isSuccessful = withContext(Dispatchers.IO) {
                        authManager.registerUser(
                            RegisterRequest(
                                username,
                                password,
                                email
                            ),
                            activityContext
                        )
                        binding?.registerButtonRegister?.isClickable = true
                    }
                }

            }
        }
    }
}