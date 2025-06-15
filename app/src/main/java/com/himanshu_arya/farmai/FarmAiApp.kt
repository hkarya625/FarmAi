package com.himanshu_arya.farmai

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.himanshu_arya.chatbot.presentation.ui.gemini.GeminiScreen
import com.himanshu_arya.farmai.navigation.CameraImageScreen
import com.himanshu_arya.farmai.navigation.ImageQueryScreen

import com.himanshu_arya.farmai.screens.CameraImageScreen
import com.himanshu_arya.farmai.screens.MainScreen
import com.himanshu_arya.image_query.presentation.HealthScreen


@Composable
fun FarmAiApp(){
//    SpeechToTextScreen()
//    GeminiScreen()
//    HealthScreen()
//    CameraImageScreen()
    MainScreen()

}