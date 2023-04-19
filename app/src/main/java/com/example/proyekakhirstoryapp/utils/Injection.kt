package com.example.proyekakhirstoryapp.utils

import android.content.Context
import com.example.proyekakhirstoryapp.BuildConfig
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiConfig
import com.example.proyekakhirstoryapp.data.datastore.SettingPreference
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import com.example.proyekakhirstoryapp.ui.login.dataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Injection {

    fun provideUserRepository(context: Context): UserRepository {

        val pref = SettingPreference.getInstance(context.dataStore)

        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val apiService = ApiConfig.getApiService(client)


        return UserRepository.getInstance(pref, apiService)
    }

}