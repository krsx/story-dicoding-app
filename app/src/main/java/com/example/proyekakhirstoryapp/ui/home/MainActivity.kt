package com.example.proyekakhirstoryapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.databinding.ActivityMainBinding
import com.example.proyekakhirstoryapp.ui.settings.SettingsActivity
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.example.proyekakhirstoryapp.ui.addstory.AddStoryActivity
import com.example.proyekakhirstoryapp.ui.home.adapter.ListStoryAdapter
import com.example.proyekakhirstoryapp.ui.home.adapter.LoadingStateListStoryAdapter

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private lateinit var listStoryAdapter: ListStoryAdapter

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        setupActionBar()

        setStoriesData()

        binding.fabAddStory.setOnClickListener{
            val intentToAddStory = Intent(this, AddStoryActivity::class.java)
            startActivity(intentToAddStory)
        }
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

    private fun setStoriesData() {
        mainViewModel.getUserToken().observe(this) { token ->
            mainViewModel.getUserStories(token)
            Log.e("Home", "Token: $token")
            initRecyclerView()
        }
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        listStoryAdapter = ListStoryAdapter()

        mainViewModel.userStories.observe(this){
            listStoryAdapter.submitData(lifecycle, it)
        }

        binding.rvStories.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateListStoryAdapter{
                listStoryAdapter.retry()
            }
        )

        listStoryAdapter.setOnItemClickCallback(object: ListStoryAdapter.OnItemClickCallback{
            override fun onItemClicked(stories: StoryModel?) {

            }
        })
    }

    private fun setupActionBar(){
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.app_name)
    }
}