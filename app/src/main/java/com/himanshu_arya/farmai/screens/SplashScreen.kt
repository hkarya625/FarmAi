package com.himanshu_arya.farmai.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshu_arya.farmai.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    // Animation triggers
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }

    // Trigger animations with delay
    LaunchedEffect(true) {
        logoVisible = true
        delay(500) // Delay for logo animation
        textVisible = true
    }

    // Logo animations
    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val logoScale by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    // Text animation (slide in from bottom)
    val textOffsetY by animateDpAsState(
        targetValue = if (textVisible) 0.dp else 30.dp,
        animationSpec = tween(durationMillis = 800)
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800)
    )

    // Layout
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    alpha = logoAlpha
                    scaleX = logoScale
                    scaleY = logoScale
                },
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )

        Spacer(modifier = Modifier.size(8.dp))

        Row(
            modifier = Modifier
                .offset(y = textOffsetY)
                .alpha(textAlpha)
        ) {
            Text(
                text = "Farm",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = colorResource(R.color.logo_color_2),
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(Modifier.size(5.dp))
            Text(
                text = "Ai",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = colorResource(R.color.logo_color_1),
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen()
}