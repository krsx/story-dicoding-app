package com.example.proyekakhirstoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.proyekakhirstoryapp.data.api.response.DefaultResponse
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.data.api.response.LoginResponse
import com.example.proyekakhirstoryapp.data.api.response.StoriesResponse
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiService
import com.example.proyekakhirstoryapp.data.datastore.SettingPreference
import retrofit2.Call

class UserRepository(
    private val pref: SettingPreference,
    private val apiService: ApiService,
) {
    fun userLogin(email: String, password: String): Call<LoginResponse> {
        return apiService.loginUser(email, password)
    }

    fun registerLogin(name: String, email: String, password: String): Call<DefaultResponse> {
        return apiService.registerUser(name, email, password)
    }

    fun getUserToken(): LiveData<String> {
        return pref.getUserToken().asLiveData()
    }

    suspend fun saveUserToken(token:String){
        return pref.saveUserToken(token)
    }

    fun getAllStories(token: String): Call<StoriesResponse> {
        return apiService.getAllStories("bearer $token")
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        @JvmStatic
        fun getInstance(
            pref: SettingPreference,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(pref, apiService)
            }.also { instance = it }
    }
}