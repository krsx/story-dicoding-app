package com.example.proyekakhirstoryapp.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.activity.viewModels
import com.example.proyekakhirstoryapp.databinding.ActivityLoginBinding
import com.example.proyekakhirstoryapp.ui.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.register.RegisterActivity
import com.example.proyekakhirstoryapp.ui.home.MainActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class LoginActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = ViewModelFactory.getInstance(this)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.getUserToken().observe(this) { token ->
            if (token != "") {
                val intentToHome = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentToHome)
                finish()
            }
        }

        binding.tvToRegister.setOnClickListener {
            val intentRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegister)
        }

        binding.btnLogin.setOnClickListener {
            loginViewModel.loginUser(
                binding.edLoginEmail.text.toString(),
                binding.edLoginPassword.text.toString()
            )
        }

        loginViewModel.user.observe(this) { user ->
            if (user?.token != null) {
                val intentMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentMain)
                finish()
            }
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else View.GONE
    }

}