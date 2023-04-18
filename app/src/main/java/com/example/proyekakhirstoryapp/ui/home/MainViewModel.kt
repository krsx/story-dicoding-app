package com.example.proyekakhirstoryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyekakhirstoryapp.data.datastore.UserPreferences
import kotlinx.coroutines.launch

//class MainViewModel(private val pref: UserPreferences) : ViewModel() {
//    fun getUserToken(): LiveData<String> {
//        return pref.getUserToken().asLiveData()
//    }
//
//    fun saveUserToken(token: String) {
//        viewModelScope.launch {
//            pref.saveUserToken(token)
//        }
//    }
//}