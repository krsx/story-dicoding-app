package com.example.proyekakhirstoryapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyekakhirstoryapp.data.api.response.LoginResponse
import com.example.proyekakhirstoryapp.data.api.response.LoginResult
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<LoginResult?>()
    val user: LiveData<LoginResult?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

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
                        saveUserToken(_user.value?.token)

                        Log.e(TAG, "loginUser: ${response.body()}")
                        _error.value = false
                    } else {
                        Log.e(TAG, "On failure ${response.message()} + ${response.code()}")
                        _message.value = response.message()
                        _error.value = true
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "On failure ${t.message.toString()}")

                    _message.value = t.message.toString()
                    _error.value = true
                }
            }
        )
    }

    fun saveUserToken(token: String?) {
        viewModelScope.launch {
            if (token != null) {
                userRepository.saveUserToken(token)
            }
        }
    }

    fun getUserToken(): LiveData<String> {
        return userRepository.getUserToken()
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }

}