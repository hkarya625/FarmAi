package com.himanshu_arya.voice_input.domain.repository

interface SpeechToTextRepository {
    suspend fun transcribeAudio(audioBytes: ByteArray): Result<String>
    fun closeResources() // Add a method to signal resource cleanup
}