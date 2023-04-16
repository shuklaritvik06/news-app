package com.example.newsapp.api

import com.example.newsapp.country
import com.example.newsapp.models.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @Headers("X-Api-Key: 4a03ed33850e423085600e3bfcc03bfc","User-Agent: Mozilla/5.0")
    @GET("top-headlines")
    fun getNews(@Query("country") country: String): Call<NewsData>
}