package com.xyz.pomotrack.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["project_id"]),
        Index(value = ["status"]),
        Index(value = ["due_date"]),
        Index(value = ["completed_at"]),
        Index(value = ["project_id", "status"])
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "project_id")
    val projectId: Long,
    val title: String,
    val description: String? = null,
    val status: TaskStatus = TaskStatus.TODO,
    @ColumnInfo(name = "due_date")
    val dueDate: Date? = null,
    @ColumnInfo(name = "estimated_minutes")
    val estimatedMinutes: Int = 25,
    @ColumnInfo(name = "estimated_pomodoros")
    val estimatedPomodoros: Int? = null,
    @ColumnInfo(name = "completed_pomodoros")
    val completedPomodoros: Int = 0,
    @ColumnInfo(name = "sort_order")
    val sortOrder: Int = 0,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    @ColumnInfo(name = "completed_at")
    val completedAt: Date? = null,
    @ColumnInfo(name = "archived_at")
    val archivedAt: Date? = null
)
