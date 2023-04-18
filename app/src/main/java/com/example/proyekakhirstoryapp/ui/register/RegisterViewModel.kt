package com.example.proyekakhirstoryapp.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.data.api.response.RegisterResponse
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(mApplication: Application) : ViewModel() {
    var errorResponse: String = ""
    var error: String = ""

    companion object {
        private const val TAG = "RegisterViewModel"
    }

    private val _user = MutableLiveData<String?>()
    val user: LiveData<String?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(
            object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _user.value = response.body()?.message
                    } else {
                        errorResponse = "On failure ${response.message()} + ${response.code()}"
                        Log.e(RegisterViewModel.TAG, errorResponse)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    error = "On failure ${t.message.toString()}"
                    Log.e(RegisterViewModel.TAG, error)
                }

            }
        )
    }
}