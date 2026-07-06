package com.xyz.pomotrack.Data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import java.util.Date

data class ProjectWithTasks(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "project_id"
    )
    val tasks: List<TaskEntity>
)

data class TaskWithSessions(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val sessions: List<FocusSessionEntity>
)

data class ProjectSummaryRow(
    @ColumnInfo(name = "project_id")
    val projectId: Long,
    @ColumnInfo(name = "project_name")
    val projectName: String,
    @ColumnInfo(name = "total_tasks")
    val totalTasks: Int,
    @ColumnInfo(name = "completed_tasks")
    val completedTasks: Int
)

data class PeriodProductivityStatsRow(
    @ColumnInfo(name = "completed_tasks")
    val completedTasks: Int,
    @ColumnInfo(name = "total_focus_sessions")
    val totalFocusSessions: Int,
    @ColumnInfo(name = "total_focus_seconds")
    val totalFocusSeconds: Long,
    @ColumnInfo(name = "active_projects")
    val activeProjects: Int
)

data class WeeklyFocusChartRow(
    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: String,
    @ColumnInfo(name = "total_focus_seconds")
    val totalFocusSeconds: Long
)

data class TopTaskFocusRow(
    @ColumnInfo(name = "task_id")
    val taskId: Long,
    @ColumnInfo(name = "task_title")
    val taskTitle: String,
    @ColumnInfo(name = "project_id")
    val projectId: Long,
    @ColumnInfo(name = "project_name")
    val projectName: String,
    @ColumnInfo(name = "total_focus_seconds")
    val totalFocusSeconds: Long
)

data class TaskWithProjectRow(
    @ColumnInfo(name = "task_id")
    val taskId: Long,
    @ColumnInfo(name = "project_id")
    val projectId: Long,
    @ColumnInfo(name = "project_name")
    val projectName: String,
    @ColumnInfo(name = "project_description")
    val projectDescription: String?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "status")
    val status: TaskStatus,
    @ColumnInfo(name = "due_date")
    val dueDate: Date?,
    @ColumnInfo(name = "estimated_minutes")
    val estimatedMinutes: Int,
    @ColumnInfo(name = "estimated_pomodoros")
    val estimatedPomodoros: Int?,
    @ColumnInfo(name = "completed_pomodoros")
    val completedPomodoros: Int,
    @ColumnInfo(name = "sort_order")
    val sortOrder: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date,
    @ColumnInfo(name = "completed_at")
    val completedAt: Date?
)
