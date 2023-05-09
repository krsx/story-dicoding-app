package com.example.proyekakhirstoryapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.example.proyekakhirstoryapp.data.api.response.*
import com.example.proyekakhirstoryapp.data.api.retrofit.ApiService
import com.example.proyekakhirstoryapp.data.datastore.SettingPreference
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.data.db.userstory.UserStoryDatabase
import com.example.proyekakhirstoryapp.data.paging.StoryRemoteMediator
import com.example.proyekakhirstoryapp.utils.MapStyle
import com.example.proyekakhirstoryapp.utils.MapType
import com.example.proyekakhirstoryapp.utils.AppExecutors
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class UserRepository(
    private val pref: SettingPreference,
    private val apiService: ApiService,
    private val userStoryDatabase: UserStoryDatabase,
    val appExecutors: AppExecutors,
) {
    fun userLogin(email: String, password: String): Call<LoginResponse> {
        return apiService.loginUser(email, password)
    }

    fun registerLogin(name: String, email: String, password: String): Call<DefaultResponse> {
        return apiService.registerUser(name, email, password)
    }

    suspend fun logout() {
        return pref.clearCache()
    }

    fun getUserToken(): LiveData<String> {
        return pref.getUserToken().asLiveData()
    }

    suspend fun saveUserToken(token: String) {
        return pref.saveUserToken(token)
    }

    fun addStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat: Float? = null,
        lon: Float? = null
    ): Call<DefaultResponse> {
        return apiService.addStory(token, photo, description, lat, lon)
    }

    fun getDetailStories(token: String, id: String): Call<DetailStoriesResponse> {
        return apiService.getDetailStories(token, id)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUserStoryList(token: String): LiveData<PagingData<StoryModel>> {
        Log.e("getUserStoryList", "run getUserStoryList")

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            remoteMediator = StoryRemoteMediator(
                userStoryDatabase, apiService, token
            ),
            pagingSourceFactory = { userStoryDatabase.userStoryDao().getAllUserStories() }).liveData
    }

    fun getStoriesMap(token: String): Call<StoriesResponse> {
        return apiService.getAllStories(token, 1)
    }

    fun getMapType(): LiveData<MapType> = pref.getMapType().asLiveData()
    suspend fun saveMapType(value: MapType) = pref.saveMapType(value)

    fun getMapStyle(): LiveData<MapStyle> = pref.getMapStyle().asLiveData()
    suspend fun saveMapStyle(value: MapStyle) = pref.saveMapStyle(value)

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        @JvmStatic
        fun getInstance(
            pref: SettingPreference,
            apiService: ApiService,
            userStoryDatabase: UserStoryDatabase,
            appExecutors: AppExecutors,
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(pref, apiService, userStoryDatabase, appExecutors)
        }.also { instance = it }
    }
}