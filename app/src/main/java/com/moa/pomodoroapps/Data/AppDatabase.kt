package com.moa.pomodoroapps.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.Data.TaskDAO

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDAO(): TaskDAO

}