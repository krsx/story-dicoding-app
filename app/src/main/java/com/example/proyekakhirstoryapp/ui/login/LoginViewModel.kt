package com.example.proyekakhirstoryapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.data.api.response.LoginResponse
import com.example.proyekakhirstoryapp.data.api.response.LoginResult
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel() : ViewModel() {
    var errorResponse: String = ""
    var error: String = ""

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _user = MutableLiveData<LoginResult?>()
    val user: LiveData<LoginResult?> = _user

    fun loginUser(email: String, password: String) {
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        _user.value = response.body()?.loginResult
                    } else {
                        errorResponse = "On failure ${response.message()} + ${response.code()}"
                        Log.e(TAG, errorResponse)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    error = "On failure ${t.message.toString()}"
                    Log.e(TAG, error)
                }

            }
        )
    }
}