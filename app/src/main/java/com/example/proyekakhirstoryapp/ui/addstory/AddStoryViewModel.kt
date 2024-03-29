package com.example.proyekakhirstoryapp.ui.addstory

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.data.api.response.DefaultResponse
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    var loc = MutableLiveData<Location?>()

    init {
        loc.value = null
    }

    fun saveLoc(location: Location?) {
        loc.value = location
    }

    fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat: Float? = null,
        lon: Float? = null
    ) {
        _isLoading.value = false
        val client = userRepository.addStory(photo, description, token, lat, lon)
        client.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    _error.value = false
                    _message.value = response.message()
                    Log.e(TAG, "uploadStory: ${response.body()}")
                } else {
                    _error.value = true
                    _message.value = response.message()
                    Log.e(TAG, "On failure ${response.message()} + ${response.code()}")

                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "On failure ${t.message.toString()}")

                _error.value = true
                _message.value = t.message.toString()
            }

        })
    }

    fun getUserToken(): LiveData<String> {
        return userRepository.getUserToken()
    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}