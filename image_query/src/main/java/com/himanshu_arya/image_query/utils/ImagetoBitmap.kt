package com.himanshu_arya.image_query.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun imageToBitmap(
    uri: Uri,
    context: Context
): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(uri)
                .allowHardware(false) // Important for Bitmap conversion
                .build()

            val result = loader.execute(request)
            val drawable = (result.drawable as? BitmapDrawable)?.bitmap
            drawable?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}