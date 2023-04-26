package com.example.proyekakhirstoryapp.ui.detailstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.data.api.response.DetailStoriesResponse
import com.example.proyekakhirstoryapp.data.api.response.Story
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _user = MutableLiveData<Story?>()
    val user: LiveData<Story?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun getDetailStory(id:String, token:String){
        _isLoading.value = true
        val client = userRepository.getDetailStories("bearer $token", id)
        client.enqueue(object: Callback<DetailStoriesResponse>{
            override fun onResponse(
                call: Call<DetailStoriesResponse>,
                response: Response<DetailStoriesResponse>
            ) {
                _isLoading.value = false
               if (response.isSuccessful){
                   _user.value = response.body()?.story
                   Log.e(TAG, "getDetailStory: ${response.body()}")
                   _error.value = false
               }else{
                   Log.e(TAG, "On failure ${response.message()} + ${response.code()}")
                   _message.value = response.message()
                   _error.value = true
               }
            }

            override fun onFailure(call: Call<DetailStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "On failure ${t.message.toString()}")

                _message.value = t.message.toString()
                _error.value = true
            }
        })
    }

    fun getUserToken(): LiveData<String> {
        return userRepository.getUserToken()
    }

    companion object{
        private const val TAG = "DetailStoryViewModel"
    }
}