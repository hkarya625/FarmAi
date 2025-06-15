package com.himanshu_arya.voice_input.data.remote


import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.*
import com.google.protobuf.ByteString
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Make it a singleton so the client is reused
class SpeechToTextRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Lazily initialize credentials and client
    private val credentials by lazy {
        val inputStream: InputStream = context.assets.open("speechToTextApi.json") // Access via raw resource
        GoogleCredentials.fromStream(inputStream).createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))
    }

    private val settings by lazy {
        SpeechSettings.newBuilder().setCredentialsProvider { credentials }.build()
    }

    // Consider making the client creation more robust or injecting it if complex setup is needed
    private val speechClient: SpeechClient by lazy {
        SpeechClient.create(settings)
    }

    suspend fun transcribeAudio(audioBytes: ByteArray): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioBytes))
                .build()

            // Configure recognition request
            val config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000) // Adjust if necessary
                .setLanguageCode("en-US")   // Adjust language
                .build()

            val request = RecognizeRequest.newBuilder()
                .setConfig(config)
                .setAudio(audio)
                .build()

            val response = speechClient.recognize(request) // Use the lazily initialized client

            // Process the response
            if (response.resultsList.isNotEmpty() && response.resultsList[0].alternativesList.isNotEmpty()) {
                Result.success(response.resultsList[0].alternativesList[0].transcript)
            } else {
                Result.success("") // Indicate no transcription found
            }
        } catch (e: Exception) {
            // Log the exception for debugging
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // Method to explicitly close the client when no longer needed (e.g., in ViewModel onCleared)
    fun closeClient() {
        if (speechClient.isShutdown) return // Avoid closing multiple times
        try {
            speechClient.shutdown()
            // Optionally wait for termination if needed
            // speechClient.awaitTermination(5, TimeUnit.SECONDS)
        } catch (e: Exception) {
            e.printStackTrace() // Log error during shutdown
        }
    }
}