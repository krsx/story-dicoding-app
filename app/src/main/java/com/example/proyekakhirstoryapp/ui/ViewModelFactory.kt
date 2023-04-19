package com.example.proyekakhirstoryapp.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyekakhirstoryapp.data.datastore.SettingPreference
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import com.example.proyekakhirstoryapp.ui.home.MainViewModel

import com.example.proyekakhirstoryapp.ui.login.LoginViewModel
import com.example.proyekakhirstoryapp.ui.register.RegisterViewModel
import com.example.proyekakhirstoryapp.utils.Injection

class ViewModelFactory private constructor(
    private val userRepository: UserRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideUserRepository(context)
                )
            }.also { INSTANCE = it }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                userRepository
            ) as T
            else -> throw java.lang.IllegalArgumentException("Unkown ViewModel class: ${modelClass.name}")
        }
    }
}