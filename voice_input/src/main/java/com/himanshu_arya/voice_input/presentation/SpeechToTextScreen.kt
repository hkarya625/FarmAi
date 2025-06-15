package com.himanshu_arya.voice_input.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.media.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
@Composable
fun SpeechToTextScreen(
    viewModel: SpeechToTextViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val transcriptionState by viewModel.transcriptionState.collectAsState()

    var isRecording by remember { mutableStateOf(false) }
    var audioRecord by remember { mutableStateOf<AudioRecord?>(null) }
    val audioData = remember { mutableStateListOf<ByteArray>() }
    val coroutineScope = rememberCoroutineScope()

    val sampleRate = 16000
    val channelConfig = AudioFormat.CHANNEL_IN_MONO
    val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    val minBufferSize = remember {
        AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    }

    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasAudioPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val startRecording: () -> Unit = startRecording@{
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return@startRecording
        }
        if (isRecording) return@startRecording

        viewModel.resetState()
        audioData.clear()

        val recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            minBufferSize
        )
        audioRecord = recorder

        try {
            recorder.startRecording()
            isRecording = true

            coroutineScope.launch(Dispatchers.IO) {
                val buffer = ByteArray(minBufferSize)
                val outputStream = ByteArrayOutputStream()

                while (isRecording) {
                    val read = recorder.read(buffer, 0, buffer.size)
                    if (read > 0) {
                        outputStream.write(buffer, 0, read)
                    }
                }

                val recordedBytes = outputStream.toByteArray()
                withContext(Dispatchers.Main) {
                    if (recordedBytes.isNotEmpty()) {
                        viewModel.transcribe(recordedBytes)
                    } else {
                        viewModel.resetState()
                    }
                }

                outputStream.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            isRecording = false
            audioRecord = null
        }
    }

    val stopRecording: () -> Unit = stopRecording@{
        if (!isRecording) return@stopRecording

        isRecording = false
        audioRecord?.let { recorder ->
            try {
                if (recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    recorder.stop()
                }
                recorder.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        audioRecord = null
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                if (isRecording) stopRecording()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (isRecording) stopRecording()
            audioRecord?.release()
        }
    }

    // --- UI Layout ---
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (transcriptionState) {
                    is TranscriptionState.Idle -> "Press Record to Start"
                    is TranscriptionState.Loading -> "Transcribing..."
                    is TranscriptionState.Success -> (transcriptionState as TranscriptionState.Success).transcript
                    is TranscriptionState.Error -> "Error: ${(transcriptionState as TranscriptionState.Error).message}"
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { if (isRecording) stopRecording() else startRecording() },
                enabled = hasAudioPermission
            ) {
                Text(if (isRecording) "Stop Recording" else "Start Recording")
            }

            if (!hasAudioPermission) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Microphone permission is required.", color = MaterialTheme.colorScheme.error)
                Button(onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }) {
                    Text("Grant Permission")
                }
            }

            if (isRecording) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
                Text(
                    "Recording...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
