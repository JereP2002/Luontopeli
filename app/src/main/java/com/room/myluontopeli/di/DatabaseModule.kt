package com.room.myluontopeli.di

import android.content.Context
import androidx.room.Room
import com.room.myluontopeli.data.AppDatabase
import com.room.myluontopeli.data.ObservationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "luontopeli_db"
        ).build()
    }

    @Provides
    fun provideObservationDao(database: AppDatabase): ObservationDao {
        return database.observationDao()
    }
}
