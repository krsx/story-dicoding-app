package com.example.proyekakhirstoryapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyekakhirstoryapp.data.datastore.UserPreferences

import com.example.proyekakhirstoryapp.ui.login.LoginViewModel
import com.example.proyekakhirstoryapp.ui.register.RegisterViewModel

class ViewModelFactory constructor(
    private val mApplication: Application,
    private val pref: UserPreferences? = null
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return pref?.let { LoginViewModel(mApplication, it) } as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(mApplication) as T
        }
        throw java.lang.IllegalArgumentException("Unkown ViewModel class: ${modelClass.name}")
    }
}