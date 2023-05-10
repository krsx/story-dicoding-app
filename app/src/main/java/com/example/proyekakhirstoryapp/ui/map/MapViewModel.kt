package com.example.proyekakhirstoryapp.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.data.api.response.StoriesResponse
import com.example.proyekakhirstoryapp.data.repository.UserRepository
import com.example.proyekakhirstoryapp.utils.MapStyle
import com.example.proyekakhirstoryapp.utils.MapType
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var _userStories = MutableLiveData<List<ListStoryItem?>?>()
    val userStories: LiveData<List<ListStoryItem?>?> = _userStories

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

    fun getMapType() : LiveData<MapType> = userRepository.getMapType()

    fun saveMapType(mapType: MapType) {
        viewModelScope.launch {
            userRepository.saveMapType(mapType)
        }
    }

    fun getMapStyle() : LiveData<MapStyle> = userRepository.getMapStyle()

    fun saveMapStyle(mapStyle: MapStyle) {
        viewModelScope.launch {
            userRepository.saveMapStyle(mapStyle)
        }
    }


    companion object {
        private const val TAG = "MapViewModel"
    }
}