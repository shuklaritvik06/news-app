package com.example.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val apiUrl = "https://newsapi.org/v2/"
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create()).build()
    }
    val apiInterface: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}