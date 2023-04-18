package com.example.proyekakhirstoryapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.proyekakhirstoryapp.databinding.ActivityRegisterBinding
import com.example.proyekakhirstoryapp.ui.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.login.LoginActivity
import com.example.proyekakhirstoryapp.ui.home.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvToLogin.setOnClickListener {
            val intentLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        binding.btnRegister.setOnClickListener {
            userRegister(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
        }

        registerViewModel.user.observe(this) { user ->
            if (user == "User created") {
                val intentMain = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intentMain)
            }
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun userRegister(name: String, email: String, password: String) {
        registerViewModel.registerUser(name, email, password)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else View.GONE
    }
}