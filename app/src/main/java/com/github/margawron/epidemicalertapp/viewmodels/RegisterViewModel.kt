package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.auth.AuthService
import com.github.margawron.epidemicalertapp.auth.RegisterRequest
import com.github.margawron.epidemicalertapp.databinding.RegisterActivityBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel @ViewModelInject internal constructor(
    private val authService: AuthService,
    @ApplicationContext val context: Context
) : ViewModel() {
    var binding: RegisterActivityBinding? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var repeatPassword: String = ""

    fun register() {
        binding?.registerButtonRegister?.isClickable = false
        if (username.length <= 3) {
            binding?.registerInputUsername?.error =
                "Nazwa użytkownika musi mieć co najmniej 4 znaki"
        }
        if (password.length <= 4) {
            binding?.registerInputPassword?.error = "Hasło musi mieć co najmniej 5 znaków"
        }
        if (password != repeatPassword) {
            binding?.registerInputPassword?.error = "Hasła muszą się zgadzać"
            binding?.registerInputRepeatPassword?.error = "Hasła muszą się zgadzać"
        } else {
            viewModelScope.launch {
                val registerResponse = withContext(Dispatchers.IO) {
                    authService.registerUser(
                        RegisterRequest(
                            username,
                            password,
                            email
                        )
                    )
                }
                binding?.registerButtonRegister?.isClickable = true
                Toast.makeText(context, "Przycisk rejestracji został naciśnięty", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}