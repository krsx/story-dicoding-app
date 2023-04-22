package com.example.proyekakhirstoryapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.databinding.ActivityMainBinding
import com.example.proyekakhirstoryapp.ui.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.home.adapter.ListStoryAdapter
import com.example.proyekakhirstoryapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        mainViewModel.getUserToken().observe(this) { token ->
            mainViewModel.getAllStories(token)

            mainViewModel.stories.observe(this) { stories ->
                if (stories != null) {
                    setStoriesData(stories)
                }
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setStoriesData(stories: List<ListStoryItem?>) {
        val adapter = ListStoryAdapter(stories)
        binding.rvStories.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_logout -> {
                mainViewModel.logout()
                val intentToLogin = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intentToLogin)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}