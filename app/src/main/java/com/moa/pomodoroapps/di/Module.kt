package com.moa.pomodoroapps.di

import android.content.Context
import androidx.room.Room
import com.moa.pomodoroapps.Data.AppDatabase
import com.moa.pomodoroapps.Data.FocusSessionDao
import com.moa.pomodoroapps.Data.ProjectDao
import com.moa.pomodoroapps.Data.TaskDAO
import com.moa.pomodoroapps.Data.TaskEntityDao
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.Module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, "task_database")
        .addMigrations(AppDatabase.MIGRATION_3_4)
        .fallbackToDestructiveMigration()
        .openHelperFactory(BusyTimeoutFactory())
        .build()


    @Provides
    @Singleton
    fun provideLegacyTaskDAO(db: AppDatabase): TaskDAO = db.taskDAO()

    @Provides
    @Singleton
    fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()

    @Provides
    @Singleton
    fun provideTaskEntityDao(db: AppDatabase): TaskEntityDao = db.taskEntityDao()

    @Provides
    @Singleton
    fun provideFocusSessionDao(db: AppDatabase): FocusSessionDao = db.focusSessionDao()


}
