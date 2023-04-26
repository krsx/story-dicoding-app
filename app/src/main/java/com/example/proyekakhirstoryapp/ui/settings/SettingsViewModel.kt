package com.example.proyekakhirstoryapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository): ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}