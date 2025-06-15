package com.himanshu_arya.voice_input.data.repository

import com.himanshu_arya.voice_input.domain.repository.SpeechToTextRepository
import com.himanshu_arya.voice_input.data.remote.SpeechToTextRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Scope repository as singleton if appropriate
class SpeechToTextRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpeechToTextRemoteDataSource
) : SpeechToTextRepository {

    override suspend fun transcribeAudio(audioBytes: ByteArray): Result<String> {
        return remoteDataSource.transcribeAudio(audioBytes)
    }

    // Propagate the close signal to the data source
    override fun closeResources() {
        remoteDataSource.closeClient()
    }
}