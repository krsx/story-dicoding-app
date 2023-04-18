package com.example.proyekakhirstoryapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.data.api.response.LoginResult
import com.example.proyekakhirstoryapp.databinding.ActivityLoginBinding
import com.example.proyekakhirstoryapp.databinding.ActivityMainBinding
import com.example.proyekakhirstoryapp.ui.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.register.RegisterActivity
import com.example.proyekakhirstoryapp.ui.screens.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvToRegister.setOnClickListener {
            val intentRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegister)
        }

        binding.btnLogin.setOnClickListener {
            userLogin(binding.edLoginEmail.text.toString(), binding.edLoginPassword.text.toString())
        }

        loginViewModel.user.observe(this) { user ->
            if (user?.token != null) {
                val intentMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentMain)
            }
        }
    }

    private fun userLogin(email: String, password: String) {
        loginViewModel.loginUser(email, password)
    }


//    private fun getLoginData(user: LoginResult?) {
//
//    }
}