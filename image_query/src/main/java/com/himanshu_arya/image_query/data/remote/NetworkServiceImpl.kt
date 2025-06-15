package com.himanshu_arya.image_query.data.remote

import android.util.Log
import com.himanshu_arya.image_query.BuildConfig
import com.himanshu_arya.image_query.data.model.request.HealthRequest
import com.himanshu_arya.image_query.domain.model.PlantHealthDomain
import com.himanshu_arya.image_query.domain.network.NetworkService
import com.himanshu_arya.image_query.domain.network.ResultWrapper
import javax.inject.Inject


class NetworkServiceImpl @Inject constructor(
    private val apiService: ApiService
): NetworkService {
    override suspend fun plantHealth(image: String): ResultWrapper<PlantHealthDomain> {
        val request = HealthRequest(
            images = listOf(image),
            latitude = 30.0668,
            longitude = 79.0193,
            similar_images = true
        )
        return try {
            val response = apiService.plantHealth(
                apiKey = BuildConfig.PLANT_ID_API_KEY,
                request = request
            )
            val body = response.body()
            if (body != null) {
                Log.d("NetworkServiceImpl", "Success${response.body()}")
                ResultWrapper.Success(body.toDomainResponse())
            } else {
                Log.d("NetworkServiceImpl", "Success1${response.message()}")
                ResultWrapper.Failure(response.message())
            }
        }catch (e:Exception){
            ResultWrapper.Failure(e.message ?: "Unknown error occurred")
        }
    }
}