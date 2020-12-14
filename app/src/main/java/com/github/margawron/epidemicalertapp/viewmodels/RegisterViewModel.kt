package com.github.margawron.epidemicalertapp.viewmodels

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.api.auth.messages.RegisterRequest
import com.github.margawron.epidemicalertapp.databinding.RegisterActivityBinding
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class RegisterViewModel @ViewModelInject internal constructor(
    private val authManager: AuthManager,
    @ActivityContext val context: Context
) : ViewModel() {
    var binding: RegisterActivityBinding? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var repeatPassword: String = ""

    fun register() {
        when {
            username.length <= 4 -> {
                binding?.registerInputUsername?.error =
                    context.getString(R.string.too_short_login)
            }
            password.length <= 4 -> {
                binding?.registerInputPassword?.error = context.getString(R.string.too_short_password)
            }
            password != repeatPassword -> {
                binding?.registerInputPassword?.error = context.getString(R.string.password_must_match)
                binding?.registerInputRepeatPassword?.error = context.getString(R.string.password_must_match)
            }
            else -> {
                viewModelScope.launch {
                    binding?.registerButtonRegister?.isClickable = false
                    val apiResponse = withContext(Dispatchers.IO) {
                        authManager.registerUser(
                            RegisterRequest(
                                username,
                                password,
                                email
                            )
                        )
                    }
                    when(apiResponse){
                        is ApiResponse.Success -> {
                            Toast.makeText(context, context.getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                            (context as Activity).finish()
                        }
                        is ApiResponse.Error -> {
                            val errorBuilder = StringBuilder()
                            apiResponse.errors.joinTo(errorBuilder, separator = "\n") {e -> e.errorMessage}
                            with(AlertDialog.Builder(context)){
                                setMessage(errorBuilder)
                                setNegativeButton(android.R.string.ok){ dialog, _ ->
                                    dialog.dismiss()
                                }
                            }.create()
                                .show()
                        }
                    }
                    binding?.registerButtonRegister?.isClickable = true
                }
            }
        }
    }
}