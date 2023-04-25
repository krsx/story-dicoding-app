package com.example.proyekakhirstoryapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.activity.viewModels
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.databinding.ActivityLoginBinding
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.register.RegisterActivity
import com.example.proyekakhirstoryapp.ui.home.MainActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class LoginActivity() : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = ViewModelFactory.getInstance(this)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
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

            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                val msg = getString(R.string.fill_field)
                displayToast(msg)
            } else {
                loginViewModel.loginUser(
                    email,
                    password,
                )

                loginViewModel.error.observe(this) { error ->
                    if (!error) {
                        loginViewModel.user.observe(this) { user ->
                            if (user?.token != null) {
                                val intentToMain =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intentToMain)
                                finish()
                            }
                        }
                    } else {
                        val msg = getString(R.string.wrong_credential)
                        displayToast(msg)
                        binding.edLoginPassword.apply {
                            text?.clear()
                        }
                    }
                }
            }

        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false
        }else{
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
        }
    }

    private fun displayToast(msg: String) {
        return Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}