package com.example.proyekakhirstoryapp.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.data.api.response.DefaultResponse
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiConfig
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
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
        val client = userRepository.registerLogin(name, email, password)
        client.enqueue(
            object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _user.value = response.body()?.message
                    } else {
                        errorResponse = "On failure ${response.message()} + ${response.code()}"
                        Log.e(TAG, errorResponse)
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    _isLoading.value = false
                    error = "On failure ${t.message.toString()}"
                    Log.e(TAG, error)
                }

            }
        )
    }
}