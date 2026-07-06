package com.xyz.pomotrack.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Parallel schema used to stage the project/task/session refactor before switching the app over.
@Database(
    entities = [
        ProjectEntity::class,
        TaskEntity::class,
        FocusSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class, RefactorConverters::class)
abstract class RefactorAppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskEntityDao(): TaskEntityDao
    abstract fun focusSessionDao(): FocusSessionDao
}
