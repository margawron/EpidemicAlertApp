package com.github.margawron.epidemicalertapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.margawron.epidemicalertapp.auth.AuthService
import com.github.margawron.epidemicalertapp.auth.LoginRequest
import com.github.margawron.epidemicalertapp.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.view.*
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var authService: AuthService

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        view.button_login.setOnClickListener {
            showToast(view)
        }

    }

    private fun showToast(view: ConstraintLayout) {
        val loginRequest =
            LoginRequest(view.input_login.text.toString(), view.input_password.text.toString())
        val (accessToken, tokenType) = runBlocking {
            withContext(Dispatchers.IO){
                return@withContext authService.getBearerToken(loginRequest)
            }
        }
        Toast.makeText(this@AuthActivity, "$tokenType $accessToken", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}