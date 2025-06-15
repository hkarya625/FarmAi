package com.himanshu_arya.image_query.presentation


import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu_arya.image_query.domain.model.PlantHealthDomain
import com.himanshu_arya.image_query.domain.network.ResultWrapper
import com.himanshu_arya.image_query.domain.usecase.PlantHealthUseCase
import com.himanshu_arya.image_query.utils.imageBitmapToBase64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val plantHealthUseCase: PlantHealthUseCase
) : ViewModel() {

    private val _responseState = MutableStateFlow<HealthResponseEvent>(HealthResponseEvent.Nothing)
    val responseState: StateFlow<HealthResponseEvent> = _responseState // ✅ read-only

    fun getHealth(image: ImageBitmap) {
        Log.d("HealthResponse", "Request is initiated")
        viewModelScope.launch {
            _responseState.value = HealthResponseEvent.Loading
            when (val result = getHealthResponse(image)) {
                is ResultWrapper.Success -> {
                    _responseState.value = HealthResponseEvent.Success(result.value)
                    Log.d("HealthResponse", result.value.toString())
                }
                is ResultWrapper.Failure -> {
                    _responseState.value = HealthResponseEvent.Error(result.message)
                    Log.d("HealthResponse", "Error: ${result.message}")
                }
            }
        }
    }

    private suspend fun getHealthResponse(image: ImageBitmap): ResultWrapper<PlantHealthDomain> {
        return try {
            val base64Image = imageBitmapToBase64(image)
            plantHealthUseCase.invoke(base64Image)
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Unknown error"
            Log.e("HealthViewModel", "Exception in getHealthResponse: $errorMsg", e)
            ResultWrapper.Failure(errorMsg)
        }
    }
}



sealed class HealthResponseEvent{
    data object Nothing:HealthResponseEvent()
    data object Loading:HealthResponseEvent()
    data class Success(val response:PlantHealthDomain):HealthResponseEvent()
    data class Error(val message:String):HealthResponseEvent()
}