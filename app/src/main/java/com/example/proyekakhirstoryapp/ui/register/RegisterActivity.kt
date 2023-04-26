package com.example.proyekakhirstoryapp.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.databinding.ActivityRegisterBinding
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }

        private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.btnRegister.setOnClickListener {

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                val msg = getString(R.string.fill_field)
                displayToast(msg)
            } else {
                userRegister(
                    name, email, password
                )

                registerViewModel.error.observe(this) { error ->
                    if (!error) {
                        registerViewModel.user.observe(this) { user ->
                            if (user == "User created") {
                                val msg = getString(R.string.register_success)
                                displayToast(msg)

                                val intentLogin =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intentLogin)
                            }
                        }
                    } else {
                        registerViewModel.message.observe(this) { message ->
                            val msg = getString(R.string.register_failed)
                            displayToast("$message : $msg")
                        }
                    }
                }

            }
        }


        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.tvToLogin.setOnClickListener {
            val intentToLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intentToLogin)
        }
    }

    private fun userRegister(name: String, email: String, password: String) {
        registerViewModel.registerUser(name, email, password)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnRegister.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnRegister.isEnabled = true
        }
    }

    private fun displayToast(msg: String) {
        return Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}