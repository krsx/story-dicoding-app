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
import com.example.proyekakhirstoryapp.ui.settings.SettingsActivity
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.addstory.AddStoryActivity
import com.example.proyekakhirstoryapp.ui.home.adapter.ListStoryAdapter

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        initRecyclerView()

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.fabAddStory.setOnClickListener{
            val intentToAddStory = Intent(this, AddStoryActivity::class.java)
            startActivity(intentToAddStory)
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
            R.id.btn_settings -> {
                val intentToSettings = Intent(this, SettingsActivity::class.java)
                startActivity(intentToSettings)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView(){
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
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}