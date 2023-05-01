package com.example.proyekakhirstoryapp.ui.detailstory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
        playAnimation()

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
        binding.apply {
            tvDetailName.text = user?.name
            tvCreateDesc.text = resources.getString(R.string.story_created_at, user?.createdAt!!.subSequence(0, 9))
            tvDetailDescription.text = user.description
        }
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

    private fun playAnimation() {
        val user = ObjectAnimator.ofFloat(binding.tvDetailName, View.ALPHA, 1f).setDuration(500)
        val date = ObjectAnimator.ofFloat(binding.tvCreateDesc, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvDetailDescription, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(user, date, desc)
            startDelay = 300
        }.start()
    }

    companion object {
        const val KEY_ID = "key_id"
    }
}