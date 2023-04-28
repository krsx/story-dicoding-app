package com.example.proyekakhirstoryapp.ui.settings

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.proyekakhirstoryapp.databinding.ActivitySettingsBinding
import com.example.proyekakhirstoryapp.ui.login.LoginActivity
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import com.example.proyekakhirstoryapp.R
import java.util.*

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

        setupView()

        binding.btnLogout.setOnClickListener {
            settingsViewModel.logout()

            val intentToLogin = Intent(this@SettingsActivity, LoginActivity::class.java)
            intentToLogin.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentToLogin)
            finish()
        }

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.tvSelectLanguage.text = Locale.getDefault().displayName
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
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.title_settings)
    }
}