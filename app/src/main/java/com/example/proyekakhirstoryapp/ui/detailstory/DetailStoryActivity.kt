package com.example.proyekakhirstoryapp.ui.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.databinding.ActivityAddStoryBinding
import com.example.proyekakhirstoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailStoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}