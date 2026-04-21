package com.room.myluontopeli.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ObservationDao {
    @Query("SELECT * FROM observations ORDER BY timestamp DESC")
    fun getAllObservations(): Flow<List<Observation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservation(observation: Observation)

    @Delete
    suspend fun deleteObservation(observation: Observation)

    @Query("SELECT * FROM observations WHERE id = :id")
    suspend fun getObservationById(id: Int): Observation?
}
