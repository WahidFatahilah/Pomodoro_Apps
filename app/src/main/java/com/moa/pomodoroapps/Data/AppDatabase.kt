package com.moa.pomodoroapps.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.moa.pomodoroapps.Data.Project
import com.moa.pomodoroapps.Data.ProjectDAO
import java.util.*

@Database(entities = [Project::class, Task::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun projectDao(): ProjectDAO
}

class Converters {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }


}