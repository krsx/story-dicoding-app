package com.example.proyekakhirstoryapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.data.api.response.StoriesResponse
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var userStories: LiveData<PagingData<StoryModel>>

    fun getUserToken(): LiveData<String> {
        return userRepository.getUserToken()
    }

    fun getUserStories(token:String){
        userStories = userRepository.getUserStoryList(token).cachedIn(viewModelScope)
        Log.e("MainViewModel", userStories.isInitialized.toString())
    }

}