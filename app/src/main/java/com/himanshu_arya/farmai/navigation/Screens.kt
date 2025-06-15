package com.himanshu_arya.farmai.navigation

import android.net.Uri
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64

@Serializable
data class TextQueryScreen(
    val prompt: String
)

@Serializable
data class ImageQueryScreen(
    val imageUri: String
)

@Serializable
data object CameraImageScreen

@Serializable
data object ContentScreen
