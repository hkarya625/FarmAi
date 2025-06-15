package com.himanshu_arya.chatbot.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.himanshu_arya.chatbot.BuildConfig
import com.himanshu_arya.chatbot.domain.network.NetworkService
import com.himanshu_arya.chatbot.domain.network.ResultWrapper

class NetworkServiceImpl: NetworkService {
    override suspend fun generateResponse(prompt: String): ResultWrapper<String> {
        return askGemini(prompt)
    }
    private suspend inline fun askGemini(prompt: String): ResultWrapper<String> {
        return try {
            val model = GenerativeModel(
                modelName = "gemini-2.0-flash-lite",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
            val rawResponse = model.generateContent(prompt).text

            val cleanedResponse = rawResponse?.replace("*", " ")?.trim()

            if (cleanedResponse.isNullOrEmpty()) {
                ResultWrapper.Failure("Error generating response")
            } else {
                ResultWrapper.Success(cleanedResponse)
            }
        } catch (e: Exception) {
            ResultWrapper.Failure("Exception: ${e.message}")
        }
    }
}
