package com.himanshu_arya.chatbot.data.repository
import com.himanshu_arya.chatbot.domain.network.NetworkService
import com.himanshu_arya.chatbot.domain.network.ResultWrapper
import com.himanshu_arya.chatbot.domain.repository.GeminiRepository

class GeminiRepositoryImpl(
    private val dataSource: NetworkService
): GeminiRepository {
    override suspend fun getGeminiResponse(prompt: String): ResultWrapper<String> {
        return dataSource.generateResponse(prompt)
    }

}