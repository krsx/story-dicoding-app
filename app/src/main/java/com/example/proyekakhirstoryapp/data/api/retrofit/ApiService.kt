package com.example.proyekakhirstoryapp.data.api.retrofit

import com.example.proyekakhirstoryapp.data.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("/register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

//    @FormUrlEncoded
//    @Headers
//    @POST("/stories")
//    fun addStories(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<RegisterResponse>

//    @GET("/stories")
//    @Headers("Authorization: Bearer <${T}>")
//    fun getAllStories(
//
//    )
}