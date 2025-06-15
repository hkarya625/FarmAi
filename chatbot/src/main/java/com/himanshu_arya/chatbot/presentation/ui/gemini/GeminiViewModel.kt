package com.himanshu_arya.chatbot.presentation.ui.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_arya.chatbot.domain.network.ResultWrapper
import com.himanshu_arya.chatbot.domain.usecase.GetGeminiResponseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(
    private val geminiResponseUseCase: GetGeminiResponseUseCase
):ViewModel() {

    private val _responseState = MutableStateFlow<GeminiResponseEvent>(GeminiResponseEvent.Nothing)
    val responseState: StateFlow<GeminiResponseEvent> = _responseState

    fun askGemini(prompt:String){
        viewModelScope.launch {
            _responseState.value = GeminiResponseEvent.Loading
            val query = "$prompt give the answer not more than 200 words and give answer of crop related query only if there is different query than don't answer"
            val response = generateResponse(query)
            if(response.isNullOrEmpty()){
                _responseState.value = GeminiResponseEvent.Error("Error generating response")
            }else{
                _responseState.value = GeminiResponseEvent.Success(response)
            }
        }
    }

    private suspend fun generateResponse(prompt: String): String? {
        geminiResponseUseCase.execute(prompt).let { result ->
            when(result){
                is ResultWrapper.Success ->{
                    return (result).value
                }
                is ResultWrapper.Failure ->{
                    return null
                }
            }
        }
    }
}

sealed class GeminiResponseEvent{
    data object Nothing:GeminiResponseEvent()
    data object Loading:GeminiResponseEvent()
    data class Success(val response:String):GeminiResponseEvent()
    data class Error(val message:String):GeminiResponseEvent()
}