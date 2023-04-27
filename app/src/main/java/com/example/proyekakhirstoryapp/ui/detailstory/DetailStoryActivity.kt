package com.example.proyekakhirstoryapp.ui.detailstory

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.data.api.response.Story
import com.example.proyekakhirstoryapp.databinding.ActivityDetailStoryBinding
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailStoryBinding? = null
    private lateinit var factory: ViewModelFactory
    private val detailViewModel: DetailStoryViewModel by viewModels { factory }
    private val binding get() = _binding!!
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        setupView()

        detailViewModel.getUserToken().observe(this) { token ->
            id = intent.getStringExtra(KEY_ID)
            id?.let { getDetailStory(it, token) }
        }

        detailViewModel.user.observe(this) { user ->
            setDetailStory(user)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setDetailStory(user: Story?) {
        Glide.with(this).load(user?.photoUrl).into(binding.ivDetailPhoto)
        binding.tvDetailName.text = user?.name
        binding.tvCreateDesc.text = resources.getString(R.string.story_created_at, user?.createdAt!!.subSequence(0, 9))
        binding.tvDetailDescription.text = user.description
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getDetailStory(id: String, token: String) {
        detailViewModel.getDetailStory(id, token)
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
        actionBar?.title = getString(R.string.title_detail)
    }

    companion object {
        const val KEY_ID = "key_id"
    }
}