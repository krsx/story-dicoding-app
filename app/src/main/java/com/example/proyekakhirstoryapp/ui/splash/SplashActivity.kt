package com.example.proyekakhirstoryapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.proyekakhirstoryapp.databinding.ActivitySplashBinding
import com.example.proyekakhirstoryapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intentHome = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intentHome)
            finish()
        }, 3000)
    }
}