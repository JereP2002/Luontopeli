package com.example.luontopeli.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "observations")
data class Observation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val speciesName: String,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double,
    val longitude: Double,
    val imagePath: String? = null
)
