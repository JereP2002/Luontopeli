package com.example.luontopeli.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.luontopeli.ui.ObservationViewModel
import com.example.luontopeli.util.ImageAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(viewModel: ObservationViewModel = hiltViewModel()) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        CameraPreviewContent(viewModel, locationPermissionState.status.isGranted)
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Anna kameran lupa")
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun CameraPreviewContent(viewModel: ObservationViewModel, hasLocationPermission: Boolean) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    var detectedLabel by remember { mutableStateOf("Etsitään...") }
    var detectedConfidence by remember { mutableStateOf(0f) }
    var isSaving by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(executor, ImageAnalyzer { label, confidence ->
                                detectedLabel = label
                                detectedConfidence = confidence
                            })
                        }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, executor)
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = detectedLabel,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                if (detectedConfidence > 0f) {
                    Text(
                        text = "Varmuus: ${(detectedConfidence * 100).toInt()}%",
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        enabled = !isSaving,
                        onClick = {
                            isSaving = true
                            if (hasLocationPermission) {
                                fusedLocationClient.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    CancellationTokenSource().token
                                ).addOnSuccessListener { location ->
                                    val lat = location?.latitude ?: 60.1699
                                    val lon = location?.longitude ?: 24.9384
                                    viewModel.addObservation(detectedLabel, detectedConfidence, lat, lon, null)
                                    isSaving = false
                                }.addOnFailureListener {
                                    viewModel.addObservation(detectedLabel, detectedConfidence, 60.1699, 24.9384, null)
                                    isSaving = false
                                }
                            } else {
                                viewModel.addObservation(detectedLabel, detectedConfidence, 60.1699, 24.9384, null)
                                isSaving = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("Tallenna havainto")
                        }
                    }
                }
            }
        }
    }
}
