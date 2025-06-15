package com.himanshu_arya.chatbot.domain.usecase

import com.himanshu_arya.chatbot.domain.repository.GeminiRepository

class GetGeminiResponseUseCase(
    private val repository: GeminiRepository
) {
    suspend fun execute(prompt:String) = repository.getGeminiResponse(prompt)

}