package com.himanshu_arya.image_query.presentation


import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.himanshu_arya.image_query.R
import com.himanshu_arya.image_query.domain.model.PlantHealthDomain

import com.himanshu_arya.image_query.utils.imageToBitmap
import com.himanshu_arya.image_query.utils.roundToDecimals


@Composable
fun HealthScreen(
    imageUri: String,
    viewModel: HealthViewModel = hiltViewModel()
) {
    val uri = Uri.parse(imageUri)
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var hasRequested by remember(imageUri) { mutableStateOf(false) }

    val uiState = viewModel.responseState.collectAsState()
    val loading = remember { mutableStateOf(false) }
    val errorMsg = remember { mutableStateOf<String?>(null) }
    val response = remember { mutableStateOf<PlantHealthDomain?>(null) }




    LaunchedEffect(uri) {
        imageBitmap = null
        val loadedBitmap = imageToBitmap(uri, context)
        imageBitmap = loadedBitmap
        Log.d("HealthScreen", "Image loaded1: $loadedBitmap")
    }


    if(loading.value){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = Color.Blue
            )
        }
    }

    when (uiState.value) {
        is HealthResponseEvent.Loading -> {
            loading.value = true
        }

        is HealthResponseEvent.Success -> {
            loading.value = false
            errorMsg.value = null
            response.value = (uiState.value as HealthResponseEvent.Success).response
        }

        is HealthResponseEvent.Error -> {
            loading.value = false
            errorMsg.value = (uiState.value as HealthResponseEvent.Error).message
        }

        HealthResponseEvent.Nothing -> {
            loading.value = false
            errorMsg.value = null
        }
    }

    HealthContent(
        imageBitmap = imageBitmap,
        loading = loading.value,
        errorMsg = errorMsg.value,
        response = response.value,
        onButtonClick = {
            viewModel.getHealth(imageBitmap!!)
        }
    )
}


@Composable
fun HealthContent(
    imageBitmap: ImageBitmap?,
    loading: Boolean,
    errorMsg: String?,
    response: PlantHealthDomain?,
    onButtonClick:()->Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.1f)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageBitmap?.let { bitmap ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp) // 8cm in dp
                            .padding(horizontal = 3.dp, vertical = 3.dp)
                    )
                }
                if(response == null){
                    Button(
                        onClick = {
                            onButtonClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lerp(
                                colorResource(R.color.card_color_1),
                                colorResource(R.color.card_color_2),
                                0.6f
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Get Health",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            when {
                loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = colorResource(R.color.card_color_1)
                        )
                    }
                }

                errorMsg != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "An error occurred: $errorMsg", color = Color.Red)
                    }
                }
                response != null -> {
                    HealthDetails(response = response)
                }
            }
        }
    }
}


@Composable
fun HealthDetails(
    response:PlantHealthDomain
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = lerp(
                    colorResource(R.color.card_color_2),
                    colorResource(R.color.card_color_1),
                    0.8f
                ).copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp)
        ){
            HealthDetailsContent(
                heading = "Plant health status",
                titleValue1 = "Is plant" to roundToDecimals(response.healthStatus.isPlantProbability).toString(),
                titleValue2 = "Is healthy" to roundToDecimals(response.healthStatus.isHealthyProbability)
            )
            HealthDetailsContent(
                heading = "Disease",
                titleValue1 = "Name" to response.mainIssue1.name,
                titleValue2 = "Probability" to roundToDecimals(response.mainIssue1.probability)
            )
            HealthDetailsContent(
                heading = "Other Disease",
                titleValue1 = "Name" to response.mainIssue2.name,
                titleValue2 = "Probability" to roundToDecimals(response.mainIssue2.probability)
            )
        }
    }
}

@Composable
fun HealthDetailsContent(
    heading:String,
    titleValue1:Pair<String, String>,
    titleValue2: Pair<String, Double>)
{
    Text(
        modifier = Modifier.padding(8.dp),
        text = "$heading:",
        style = MaterialTheme.typography.headlineMedium,
        color = colorResource(R.color.card_color_2),
        softWrap = true
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "${titleValue1.first} -: ${titleValue1.second}",
            color = colorResource(R.color.card_color_2),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "${titleValue2.first} -: ${titleValue2.second}",
            color = colorResource(R.color.card_color_2),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}




