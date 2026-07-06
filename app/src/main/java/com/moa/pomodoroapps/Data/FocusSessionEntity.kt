package com.moa.pomodoroapps.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "focus_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["task_id"]),
        Index(value = ["started_at"]),
        Index(value = ["session_type"]),
        Index(value = ["task_id", "started_at"])
    ]
)
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "task_id")
    val taskId: Long,
    @ColumnInfo(name = "session_type")
    val sessionType: FocusSessionType = FocusSessionType.FOCUS,
    @ColumnInfo(name = "planned_minutes")
    val plannedMinutes: Int,
    @ColumnInfo(name = "actual_seconds")
    val actualSeconds: Long = 0,
    @ColumnInfo(name = "started_at")
    val startedAt: Date = Date(),
    @ColumnInfo(name = "ended_at")
    val endedAt: Date? = null,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
)
