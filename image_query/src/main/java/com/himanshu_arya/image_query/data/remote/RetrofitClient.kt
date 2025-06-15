package com.himanshu_arya.image_query.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



// object defines their is only one instance of this retrofit client
//object RetrofitClient {
//    private const val BASE_URL = "https://plant.id/api/v3/"  // Replace with the correct base URL if different
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .connectTimeout(30, TimeUnit.SECONDS)         //Max time to establish a connection to the server
//        .readTimeout(30, TimeUnit.SECONDS)
//        .writeTimeout(30, TimeUnit.SECONDS)
//        .build()
//    // These timeouts help avoid hanging requests on slow or poor networks.
//
//    val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//}