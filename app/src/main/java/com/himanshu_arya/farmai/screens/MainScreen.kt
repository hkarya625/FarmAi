package com.himanshu_arya.farmai.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.himanshu_arya.chatbot.R
import com.himanshu_arya.chatbot.presentation.ui.gemini.GeminiScreen
import com.himanshu_arya.farmai.navigation.CameraImageScreen
import com.himanshu_arya.farmai.navigation.ContentScreen
import com.himanshu_arya.farmai.navigation.ImageQueryScreen
import com.himanshu_arya.farmai.navigation.TextQueryScreen
import com.himanshu_arya.image_query.presentation.HealthScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(){
    val prompt = remember { mutableStateOf("") }
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp) // Consider making height more flexible if needed
                    .background(
                        color = lerp(
                            colorResource(R.color.card_color_2),
                            colorResource(R.color.card_color_1),
                            0.5f
                        )
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "menu", // Good content description for accessibility
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.CenterStart)
                        .clickable { /* Handle menu click if needed */ } // Added clickable for icon
                )
                Text(
                    text = "Ask To FarmAi",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.Center)
                )
                Image(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "user profile", // More specific description
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.CenterEnd)
                        .clickable { /* Handle user click if needed */ }, // Added clickable for icon
                    contentScale = ContentScale.Crop
                )
            }
        },
        bottomBar = {
            InputSection(
                sentButtonOnClick = {
                    prompt.value = it
                    navController.navigate(TextQueryScreen(it))
                },
                cameraButtonOnClick = {
                    navController.navigate(CameraImageScreen)
                },
                onMicClick = {

                }
            )
        }
    ){ it ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            shadowElevation = 3.dp)
        {
            NavHost(
                navController = navController,
                startDestination = ContentScreen
            ){

                composable<ContentScreen>{
                    ContentScreen()
                }

                composable<TextQueryScreen>{
                    val args = it.toRoute<TextQueryScreen>()
                    GeminiScreen(args.prompt)
                }

                composable<CameraImageScreen>{
                    CameraImageScreen(
                        onImageCaptured = {
                                imageUri ->
                            navController.navigate(ImageQueryScreen(imageUri = imageUri))
                        }
                    )
                }

                composable<ImageQueryScreen>{
                    val args = it.toRoute<ImageQueryScreen>()
                    HealthScreen(imageUri = args.imageUri)
                }
            }
        }
    }
}


@Composable
fun InputSection(
    sentButtonOnClick:(String)->Unit,
    cameraButtonOnClick:()->Unit,
    onMicClick:()->Unit
) {
    var query by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = lerp(
                    colorResource(R.color.card_color_1),
                    colorResource(R.color.card_color_2),
                    0.2f
                )
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Camera Icon
            Icon(
                painter = painterResource(id = R.drawable.ic_camera), // Use id =
                contentDescription = "Open Camera",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable {
                        cameraButtonOnClick()
                    }
            )

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text(
                    text ="Ask your question",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize) },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface

                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                // Add KeyboardActions to handle the IME action button click
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (query.isNotBlank()) {
                            sentButtonOnClick(query)
                            query = ""
                            keyboardController?.hide()
                        }
                    }
                )
            )

            // Send Icon
            // Visually indicate if the send button is enabled
            val isSendEnabled = query.isNotBlank()
            Icon(
                painter = painterResource(id = R.drawable.ic_sent), // Use id =
                contentDescription = if (isSendEnabled) "Send Message" else "Input field is empty", // Dynamic description
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp) // Add padding for larger touch target
                    .alpha(if (isSendEnabled) 1f else 0.5f) // Reduce alpha when disabled
                    .clickable(enabled = isSendEnabled) { // Disable click when blank
                        sentButtonOnClick(query) // Pass the current query value
                        query = ""
                        keyboardController?.hide()
                    }
            )

            // Mic Icon
            Icon(
                painter = painterResource(id = R.drawable.ic_mic), // Use id =
                contentDescription = "Voice Input", // Good content description
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp) // Add padding for larger touch target
                    .clickable {
                        onMicClick() // Call the mic click callback
                    }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewContent(){
   InputSection(
       sentButtonOnClick = {  },
       cameraButtonOnClick = { },
       onMicClick = {

       }
   ) 
}