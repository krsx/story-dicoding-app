package com.example.proyekakhirstoryapp.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import com.example.proyekakhirstoryapp.ui.addstory.AddStoryViewModel
import com.example.proyekakhirstoryapp.ui.detailstory.DetailStoryViewModel
import com.example.proyekakhirstoryapp.ui.home.MainViewModel
import com.example.proyekakhirstoryapp.ui.login.LoginViewModel
import com.example.proyekakhirstoryapp.ui.map.MapViewModel
import com.example.proyekakhirstoryapp.ui.register.RegisterViewModel
import com.example.proyekakhirstoryapp.ui.settings.SettingsViewModel
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
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> AddStoryViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> DetailStoryViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                userRepository
            ) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(
                userRepository
            ) as T
            else -> throw java.lang.IllegalArgumentException("Unkown ViewModel class: ${modelClass.name}")
        }
    }
}