package com.example.proyekakhirstoryapp.ui.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
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
        binding.tvCreateDesc.text = user?.createdAt!!.subSequence(0, 9)
        binding.tvDetailDescription.text = user.description
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getDetailStory(id: String, token: String) {
        detailViewModel.getDetailStory(id, token)
    }

    companion object {
        const val KEY_ID = "key_id"
    }
}