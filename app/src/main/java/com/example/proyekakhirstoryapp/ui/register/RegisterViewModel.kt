package com.example.proyekakhirstoryapp.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.data.api.response.DefaultResponse
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<String?>()
    val user: LiveData<String?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

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
                        _error.value = false
                    } else {
                        Log.e(TAG, "On failure ${response.message()} + ${response.code()}")
                        _message.value = response.message()
                        _error.value = true
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "On failure ${t.message.toString()}")

                    _message.value = t.message.toString()
                    _error.value = true
                }

            }
        )
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}