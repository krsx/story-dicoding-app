package com.example.proyekakhirstoryapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.proyekakhirstoryapp.data.datastore.UserPreferences
import com.example.proyekakhirstoryapp.databinding.ActivityLoginBinding
import com.example.proyekakhirstoryapp.ui.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.register.RegisterActivity
import com.example.proyekakhirstoryapp.ui.home.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_token")

class LoginActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(application, pref)
        )[LoginViewModel::class.java]


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