package com.himanshu_arya.image_query.domain.network

import com.himanshu_arya.image_query.domain.model.PlantHealthDomain
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface NetworkService {
    suspend fun plantHealth(image:String):ResultWrapper<PlantHealthDomain>
}
sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val message: String) : ResultWrapper<Nothing>()
}