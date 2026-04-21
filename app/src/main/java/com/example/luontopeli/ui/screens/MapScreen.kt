package com.example.luontopeli.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.luontopeli.ui.ObservationViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: ObservationViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val observations by viewModel.observations.collectAsState()

    // Initialize osmdroid configuration
    remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Löytökartta") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(12.0)
                    controller.setCenter(GeoPoint(60.1699, 24.9384)) // Default to Helsinki
                }
            },
            modifier = Modifier.fillMaxSize().padding(padding),
            update = { mapView ->
                mapView.overlays.clear()
                observations.forEach { observation ->
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(observation.latitude, observation.longitude)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = observation.speciesName
                    marker.snippet = "Varmuus: ${(observation.confidence * 100).toInt()}%"
                    mapView.overlays.add(marker)
                }
                mapView.invalidate()
            }
        )
    }
}
