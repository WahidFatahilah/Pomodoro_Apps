package com.moa.pomodoroapps.infrastructure

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moa.pomodoroapps.domain.Break
import com.moa.pomodoroapps.domain.Pomodoro
import com.moa.pomodoroapps.domain.Project
import com.moa.pomodoroapps.domain.Task
import com.moa.pomodoroapps.repository.DAO.BreakDao
import com.moa.pomodoroapps.repository.DAO.PomodoroDao
import com.moa.pomodoroapps.repository.DAO.ProjectDao
import com.moa.pomodoroapps.repository.DAO.TaskDao

@Database(entities = [Project::class, Break::class, Task::class, Pomodoro::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun breakDao(): BreakDao
    abstract fun taskDao(): TaskDao
    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pomodoro_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}