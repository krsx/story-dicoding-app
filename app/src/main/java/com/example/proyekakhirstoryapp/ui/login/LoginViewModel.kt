package com.example.proyekakhirstoryapp.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.proyekakhirstoryapp.data.api.response.LoginResponse
import com.example.proyekakhirstoryapp.data.api.response.LoginResult
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiConfig
import com.example.proyekakhirstoryapp.data.datastore.SettingPreference
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    var errorResponse: String = ""
    var error: String = ""

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _user = MutableLiveData<LoginResult?>()
    val user: LiveData<LoginResult?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        val client = userRepository.userLogin(email, password)
        client.enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _user.value = response.body()?.loginResult
                    } else {
                        errorResponse = "On failure ${response.message()} + ${response.code()}"
                        Log.e(TAG, errorResponse)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    error = "On failure ${t.message.toString()}"
                    Log.e(TAG, error)
                }

            }
        )
    }

}