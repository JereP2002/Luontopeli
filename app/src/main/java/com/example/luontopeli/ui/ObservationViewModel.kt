package com.example.luontopeli.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luontopeli.data.Observation
import com.example.luontopeli.data.ObservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObservationViewModel @Inject constructor(
    private val repository: ObservationRepository
) : ViewModel() {

    val observations: StateFlow<List<Observation>> = repository.getAllObservations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addObservation(speciesName: String, confidence: Float, latitude: Double, longitude: Double, imagePath: String?) {
        viewModelScope.launch {
            repository.insertObservation(
                Observation(
                    speciesName = speciesName,
                    confidence = confidence,
                    latitude = latitude,
                    longitude = longitude,
                    imagePath = imagePath
                )
            )
        }
    }

    fun deleteObservation(observation: Observation) {
        viewModelScope.launch {
            repository.deleteObservation(observation)
        }
    }
}
