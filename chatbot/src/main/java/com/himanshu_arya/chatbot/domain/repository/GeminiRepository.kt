package com.himanshu_arya.chatbot.domain.repository

import com.himanshu_arya.chatbot.domain.network.ResultWrapper

interface GeminiRepository {
    suspend fun getGeminiResponse(prompt:String):ResultWrapper<String>
}