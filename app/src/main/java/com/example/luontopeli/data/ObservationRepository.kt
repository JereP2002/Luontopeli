package com.example.luontopeli.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObservationRepository @Inject constructor(
    private val observationDao: ObservationDao
) {
    fun getAllObservations(): Flow<List<Observation>> = observationDao.getAllObservations()

    suspend fun insertObservation(observation: Observation) {
        observationDao.insertObservation(observation)
    }

    suspend fun deleteObservation(observation: Observation) {
        observationDao.deleteObservation(observation)
    }

    suspend fun getObservationById(id: Int): Observation? {
        return observationDao.getObservationById(id)
    }
}
