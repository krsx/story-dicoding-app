package com.example.proyekakhirstoryapp.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.proyekakhirstoryapp.databinding.ActivitySettingsBinding
import com.example.proyekakhirstoryapp.ui.login.LoginActivity
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private var _binding: ActivitySettingsBinding? = null
    private lateinit var factory: ViewModelFactory
    private val settingsViewModel: SettingsViewModel by viewModels { factory }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = ViewModelFactory.getInstance(this)

        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            settingsViewModel.logout()

            val intentToLogin = Intent(this@SettingsActivity, LoginActivity::class.java)
            startActivity(intentToLogin)
            finish()
        }
    }
}