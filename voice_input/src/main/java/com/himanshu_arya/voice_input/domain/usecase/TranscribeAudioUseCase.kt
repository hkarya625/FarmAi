package com.himanshu_arya.voice_input.domain.usecase

import com.himanshu_arya.voice_input.domain.repository.SpeechToTextRepository
import javax.inject.Inject

class TranscribeAudioUseCase @Inject constructor(
    private val repository: SpeechToTextRepository
) {
    suspend operator fun invoke(audioBytes: ByteArray): Result<String> {
        // Add any domain-specific logic here if needed before/after calling repo
        return repository.transcribeAudio(audioBytes)
    }
}