package com.moa.pomodoroapps.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Projects")
data class Project(
    @PrimaryKey val id: Long,
    val name: String,
    val created_at: Date,
    val completed_percent: Float
)

@Entity(tableName = "Breaks", foreignKeys = [
    ForeignKey(entity = Pomodoro::class, parentColumns = ["id"], childColumns = ["pomodoro_id"])
])
data class Break(
    @PrimaryKey val id: Long,
    val pomodoro_id: Long,
    val start_time: Date,
    val end_time: Date
)

@Entity(tableName = "Tasks", foreignKeys = [
    ForeignKey(entity = Project::class, parentColumns = ["id"], childColumns = ["project_id"])
])
data class Task(
    @PrimaryKey val id: Long,
    val project_id: Long,
    val name: String,
    val created_at: Date,
    val is_completed: Boolean
)

@Entity(tableName = "Pomodoros", foreignKeys = [
    ForeignKey(entity = Task::class, parentColumns = ["id"], childColumns = ["task_id"])
])
data class Pomodoro(
    @PrimaryKey val id: Long,
    val task_id: Long,
    val start_time: Date,
    val end_time: Date
)