package com.himanshu_arya.chatbot.domain.network

interface NetworkService {
    suspend fun generateResponse(prompt: String): ResultWrapper<String>
}

sealed class ResultWrapper<out T> {
    data class Success(val value: String) : ResultWrapper<String>()
    data class Failure(val message: String) : ResultWrapper<Nothing>()
}