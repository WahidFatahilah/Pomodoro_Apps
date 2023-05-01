package com.moa.pomodoroapps.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.moa.pomodoroapps.Data.AppDatabase
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, "task_database").build()


    @Provides
    fun provideDAO(db: AppDatabase) = db.taskDAO()


}