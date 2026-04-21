package com.example.luontopeli.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Observation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun observationDao(): ObservationDao
}
