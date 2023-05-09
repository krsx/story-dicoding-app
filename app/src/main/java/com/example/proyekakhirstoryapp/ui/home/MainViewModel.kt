package com.example.proyekakhirstoryapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.data.repository.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var userStories: LiveData<PagingData<StoryModel>>

    fun getUserToken(): LiveData<String> {
        return userRepository.getUserToken()
    }

    fun getUserStories(token:String){
        userStories = userRepository.getUserStoryList(token).cachedIn(viewModelScope)
//        Log.e("MainViewModel", userStories.isInitialized.toString())
    }

}