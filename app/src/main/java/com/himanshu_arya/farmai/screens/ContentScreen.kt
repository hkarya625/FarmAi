package com.himanshu_arya.farmai.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.himanshu_arya.farmai.R
import kotlinx.coroutines.delay

@Composable
fun ContentScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            modifier = Modifier
                .size(90.dp),
            contentDescription = "logo",
        )
//        Row{
//            Text(
//                text = "Farm",
//                style = MaterialTheme.typography.headlineMedium.copy(
//                    color = colorResource(R.color.logo_color_2),
//                    fontWeight = FontWeight.SemiBold
//                )
//            )
//            Spacer(Modifier.size(5.dp))
//            Text(
//                text = "Ai",
//                style = MaterialTheme.typography.headlineMedium.copy(
//                    color = colorResource(R.color.logo_color_1),
//                    fontWeight = FontWeight.SemiBold
//                )
//            )
//        }


        var visibleText by remember { mutableStateOf("") }
        var wordType by remember { mutableStateOf("ai") }
        var word by remember { mutableStateOf("Ask to chatbot!") }
        LaunchedEffect(key1 = wordType) {
            while (true) {
                for (i in 1..word.length) {
                    visibleText = word.substring(0, i)
                    delay(100L)
                }
                delay(1000L)

                wordType = if (wordType == "ai") "health" else "ai"
                word = if (wordType == "ai") "Ask to chatbot!" else "Detect plant disease!"
                visibleText = ""
                delay(300L)
            }
        }

        Text(
            text = visibleText,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = lerp(
                    colorResource(R.color.logo_color_1),
                    colorResource(R.color.logo_color_2),
                    0.4f
                )
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewContentScreen(){
    Card(
        modifier = Modifier
            .widthIn(max = 280.dp) // Limit bubble width
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(com.himanshu_arya.chatbot.R.color.card_color_1).copy(alpha = 0.5f)
        )
    ) {
        Text(
            "TExt"
        )
    }
}