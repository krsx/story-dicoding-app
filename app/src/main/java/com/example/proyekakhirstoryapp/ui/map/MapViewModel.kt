package com.example.proyekakhirstoryapp.ui.map

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.data.api.response.StoriesResponse
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import com.example.proyekakhirstoryapp.ui.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val userRepository: UserRepository) : ViewModel() {
    var myLocationPermission = MutableLiveData<Boolean>()

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var _userStories = MutableLiveData<List<ListStoryItem?>?>()
    val userStories: LiveData<List<ListStoryItem?>?> = _userStories

    fun setMyLocationPermission(value: Boolean) {
        myLocationPermission.value = value
    }

    fun getUserToken(): LiveData<String> = userRepository.getUserToken()

    fun getUserStoryMap(token: String) {
        val formatToken = "Bearer $token"
        val client = userRepository.getStoriesMap(formatToken)
        client.enqueue(
            object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        val userResponse = response.body()?.listStory
                        Log.e(TAG, "$userResponse")
                        userRepository.appExecutors.networkIO.execute {
                            _userStories.postValue(userResponse)
                        }
                    } else {
                        Log.e(TAG, "On failure ${response.message()} + ${response.code()}")
                        _message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    _message.value = t.message
                    Log.e(TAG, "On failure ${t.message.toString()}")
                }
            }
        )
    }

    companion object {
        private const val TAG = "MapViewModel"
    }
}