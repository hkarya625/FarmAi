package com.himanshu_arya.image_query.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

fun imageBitmapToBase64(imageBitmap: ImageBitmap): String {
    val bitmap: Bitmap = imageBitmap.asAndroidBitmap()

    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.WEBP, 100, outputStream)  // 100 = quality max


    val byteArray = outputStream.toByteArray()


    val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

    return "data:image/webp;base64,$base64String"
}
fun base64ToImageBitmap(base64: String): ImageBitmap? {
    return try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}