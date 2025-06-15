package com.himanshu_arya.image_query.data.remote

import com.himanshu_arya.image_query.data.model.request.HealthRequest
import com.himanshu_arya.image_query.data.model.response.HealthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("health_assessment")
    suspend fun plantHealth(
        @Header("Api-Key") apiKey: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: HealthRequest
    ): Response<HealthResponse>
}