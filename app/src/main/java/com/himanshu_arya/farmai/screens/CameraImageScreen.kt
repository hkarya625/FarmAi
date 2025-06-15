package com.himanshu_arya.farmai.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.himanshu_arya.farmai.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun CameraImageScreen(
    onImageCaptured: (String) -> Unit
){
    val context = LocalContext.current
    val file = context.createImageFile()       // create temp image file where we will store image
    val uri = FileProvider.getUriForFile(                                                   // create secure content uri for file
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember {                                                      // place where we store uri of image
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {  // launcher for camera and collect the result
        capturedImageUri = uri                                                                      // update the uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if(it){
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }else{
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var showDialog by remember { mutableStateOf(true) }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = "Open Camera")
                },
                text = {
                    Text("Are you sure you want to proceed?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if(permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                            cameraLauncher.launch(uri)
                        }else{
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        } }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("No")
                    }
                }
            )
        }
//        Button(
//            onClick = {
//                val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
//                if(permissionCheckResult == PackageManager.PERMISSION_GRANTED){
//                    cameraLauncher.launch(uri)
//                }else{
//                    permissionLauncher.launch(Manifest.permission.CAMERA)
//                }
//            }
//        ) {
//            Text(text = "Take image")
//        }
    }

    if (capturedImageUri.path?.isNotEmpty() == true){
        onImageCaptured(capturedImageUri.toString())
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile():File{
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(                  // create a temp .jpg file
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}