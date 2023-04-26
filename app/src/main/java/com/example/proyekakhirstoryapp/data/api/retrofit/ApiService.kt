package com.example.proyekakhirstoryapp.data.api.retrofit

import com.example.proyekakhirstoryapp.data.api.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 0,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null,
    ): Call<DefaultResponse>

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoriesResponse>
}