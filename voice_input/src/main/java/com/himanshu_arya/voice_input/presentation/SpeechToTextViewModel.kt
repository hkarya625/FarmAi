package com.himanshu_arya.voice_input.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_arya.voice_input.domain.repository.SpeechToTextRepository
import com.himanshu_arya.voice_input.domain.usecase.TranscribeAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeechToTextViewModel @Inject constructor(
    private val transcribeAudioUseCase: TranscribeAudioUseCase,
    private val repository: SpeechToTextRepository // Inject repository for cleanup
) : ViewModel() {

    private val _transcriptionState = MutableStateFlow<TranscriptionState>(TranscriptionState.Idle)
    val transcriptionState: StateFlow<TranscriptionState> = _transcriptionState.asStateFlow()

    fun transcribe(audioBytes: ByteArray) {
        _transcriptionState.value = TranscriptionState.Loading // Update state to Loading

        if (audioBytes.isEmpty()) {
            _transcriptionState.value = TranscriptionState.Error("Audio data is empty.")
            return
        }

        viewModelScope.launch {
            transcribeAudioUseCase(audioBytes)
                .onSuccess { transcription ->
                    if (transcription.isBlank()) {
                        _transcriptionState.value =
                            TranscriptionState.Success("[No speech detected]")
                    } else {
                        _transcriptionState.value = TranscriptionState.Success(transcription)
                    }
                }
                .onFailure { exception ->
                    _transcriptionState.value =
                        TranscriptionState.Error("Transcription failed: ${exception.message}")
                    exception.printStackTrace() // Log for debugging
                }
        }
    }

    fun resetState() {
        _transcriptionState.value = TranscriptionState.Idle
    }

    // Clean up resources when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        repository.closeResources() // Close the SpeechClient via the repository
    }
}

// Define states for the UI
sealed class TranscriptionState {
    object Idle : TranscriptionState()
    object Loading : TranscriptionState()
    data class Success(val transcript: String) : TranscriptionState()
    data class Error(val message: String) : TranscriptionState()
}