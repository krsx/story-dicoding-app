package com.example.proyekakhirstoryapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.data.api.response.StoriesResponse
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import com.example.proyekakhirstoryapp.ui.login.LoginViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    var errorResponse: String = ""
    var error: String = ""

    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
    val stories: LiveData<List<ListStoryItem?>?> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories(token: String) {
        _isLoading.value = true
        val client = userRepository.getAllStories(token)
        client.enqueue(
            object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _stories.value = response.body()?.listStory
                        Log.e(TAG, _stories.value.toString())
                    } else {
                        errorResponse = "On failure ${response.message()} + ${response.code()}"
                        Log.e(TAG, errorResponse)
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    _isLoading.value = false
                    error = "On failure ${t.message.toString()}"
                    Log.e(TAG, error)
                }

            }
        )
    }

    fun getUserToken(): LiveData<String> {
        return userRepository.getUserToken()
    }



    companion object {
        private const val TAG = "MainViewModel"
    }
}