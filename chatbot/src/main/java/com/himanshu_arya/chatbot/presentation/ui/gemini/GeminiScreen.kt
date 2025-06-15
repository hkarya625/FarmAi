package com.himanshu_arya.chatbot.presentation.ui.gemini

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.himanshu_arya.chatbot.R
import kotlinx.coroutines.delay

@Composable
fun GeminiScreen(
    prompt:String = "",
    viewModel: GeminiViewModel = hiltViewModel()
) {
    val uiState = viewModel.responseState.collectAsState()
    LaunchedEffect(prompt) {
        viewModel.askGemini(prompt)
    }
    val loading = remember { mutableStateOf(false) }
    val errorMsg = remember { mutableStateOf<String?>(null) }
    val nothing = remember { mutableStateOf(true) }
    val response = remember { mutableStateOf("") }


    when(uiState.value){
        is GeminiResponseEvent.Loading ->{
            loading.value = true
            errorMsg.value = null
        }
        is GeminiResponseEvent.Success -> {
            loading.value = false
            response.value = (uiState.value as GeminiResponseEvent.Success).response
            nothing.value = false
        }
        is GeminiResponseEvent.Error -> {
            loading.value = false
            errorMsg.value = (uiState.value as GeminiResponseEvent.Error).message
        }
        GeminiResponseEvent.Nothing ->{

        }
    }
    MainContent(
        loading = loading.value,
        errorMsg = errorMsg.value,
        prompt = prompt,
        response = response.value,
        nothing = nothing.value
    )
}

@Composable
fun MainContent(
    loading: Boolean = false,
    errorMsg: String?,
    prompt: String,
    response: String,
    nothing:Boolean
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray.copy(alpha = 0.1f))
    ) {
        item {
            when {
                errorMsg != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text =  "An error occurred", color = Color.Red)
                    }
                }
                else -> {
                    MessageBubble(text = prompt, isUser = true)
                    MessageBubble(text = response, isUser = false, isLoading = loading)
                }
            }
        }
    }
}
@Composable
fun MessageBubble(
    text: String,
    isUser: Boolean,
    isLoading:Boolean = false
) {

    var visibleText by remember { mutableStateOf("") }

    // Typing animation effect
    LaunchedEffect(text) {
        visibleText = ""
        text.forEachIndexed { i, _ ->
            visibleText = text.substring(0, i + 1)
            delay(15L) // Speed of typing (adjust as needed)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (isUser) Spacer(modifier = Modifier.weight(1f)) // Push message to right half

        Card(
            modifier = Modifier
                .widthIn(max = 280.dp) // Limit bubble width
                .clip(RoundedCornerShape(16.dp)),
            backgroundColor = if (isUser) colorResource(R.color.card_color_2) else colorResource(R.color.card_color_1).copy(alpha = 0.5f)
        ) {
            if (isLoading)
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = colorResource(R.color.card_color_2)
                    )
                }
            }
            else
            {
                Text(
                    text = visibleText,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(16.dp),
                    color = if (isUser) Color.White else Color.Black,
                    softWrap = true
                )
            }
        }

        if (!isUser) Spacer(modifier = Modifier.weight(1f)) // Push message to left half
    }
}




