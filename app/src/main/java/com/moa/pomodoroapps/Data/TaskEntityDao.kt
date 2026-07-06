package com.moa.pomodoroapps.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TaskEntityDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTasks(tasks: List<TaskEntity>): List<Long>

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query(
        """
        SELECT * FROM tasks
        WHERE id = :taskId
        LIMIT 1
        """
    )
    fun getTaskById(taskId: Long): Flow<TaskEntity?>

    @Query(
        """
        SELECT
            t.id AS task_id,
            t.project_id AS project_id,
            p.name AS project_name,
            p.description AS project_description,
            t.title AS title,
            t.description AS description,
            t.status AS status,
            t.due_date AS due_date,
            t.estimated_minutes AS estimated_minutes,
            t.estimated_pomodoros AS estimated_pomodoros,
            t.completed_pomodoros AS completed_pomodoros,
            t.sort_order AS sort_order,
            t.created_at AS created_at,
            t.updated_at AS updated_at,
            t.completed_at AS completed_at
        FROM tasks t
        INNER JOIN projects p ON p.id = t.project_id
        WHERE t.archived_at IS NULL
          AND p.archived_at IS NULL
        ORDER BY p.updated_at DESC, t.sort_order ASC, t.created_at ASC
        """
    )
    fun getAllTasksWithProject(): Flow<List<TaskWithProjectRow>>

    @Query(
        """
        SELECT
            t.id AS task_id,
            t.project_id AS project_id,
            p.name AS project_name,
            p.description AS project_description,
            t.title AS title,
            t.description AS description,
            t.status AS status,
            t.due_date AS due_date,
            t.estimated_minutes AS estimated_minutes,
            t.estimated_pomodoros AS estimated_pomodoros,
            t.completed_pomodoros AS completed_pomodoros,
            t.sort_order AS sort_order,
            t.created_at AS created_at,
            t.updated_at AS updated_at,
            t.completed_at AS completed_at
        FROM tasks t
        INNER JOIN projects p ON p.id = t.project_id
        WHERE t.id = :taskId
          AND t.archived_at IS NULL
          AND p.archived_at IS NULL
        LIMIT 1
        """
    )
    fun getTaskWithProjectById(taskId: Long): Flow<TaskWithProjectRow?>

    @Transaction
    @Query(
        """
        SELECT * FROM tasks
        WHERE id = :taskId
        LIMIT 1
        """
    )
    fun getTaskWithSessions(taskId: Long): Flow<TaskWithSessions?>

    @Query(
        """
        SELECT * FROM tasks
        WHERE project_id = :projectId
          AND archived_at IS NULL
        ORDER BY sort_order ASC, created_at ASC
        """
    )
    fun getTasksByProject(projectId: Long): Flow<List<TaskEntity>>

    @Query(
        """
        SELECT * FROM tasks
        WHERE due_date >= :start
          AND due_date < :end
          AND archived_at IS NULL
        ORDER BY due_date ASC, sort_order ASC
        """
    )
    fun getTasksDueBetween(start: Date, end: Date): Flow<List<TaskEntity>>

    @Query(
        """
        SELECT * FROM tasks
        WHERE due_date >= :start
          AND due_date < :end
          AND archived_at IS NULL
          AND status != 'done'
        ORDER BY due_date ASC, sort_order ASC
        """
    )
    fun getActiveTasksDueBetween(start: Date, end: Date): Flow<List<TaskEntity>>

    @Query(
        """
        SELECT * FROM tasks
        WHERE project_id = :projectId
          AND archived_at IS NULL
          AND status = 'done'
        ORDER BY completed_at DESC, updated_at DESC
        """
    )
    fun getCompletedTasksByProject(projectId: Long): Flow<List<TaskEntity>>

    @Query(
        """
        UPDATE tasks
        SET status = :status,
            completed_at = :completedAt,
            updated_at = :updatedAt
        WHERE id = :taskId
        """
    )
    suspend fun updateTaskStatus(
        taskId: Long,
        status: TaskStatus,
        completedAt: Date?,
        updatedAt: Date
    )

    @Query(
        """
        UPDATE tasks
        SET sort_order = :sortOrder,
            updated_at = :updatedAt
        WHERE id = :taskId
        """
    )
    suspend fun updateTaskSortOrder(
        taskId: Long,
        sortOrder: Int,
        updatedAt: Date
    )

    @Query(
        """
        UPDATE tasks
        SET title = :title,
            description = :description,
            due_date = :dueDate,
            updated_at = :updatedAt
        WHERE id = :taskId
        """
    )
    suspend fun updateTaskContent(
        taskId: Long,
        title: String,
        description: String?,
        dueDate: Date?,
        updatedAt: Date
    )

    @Query(
        """
        UPDATE tasks
        SET archived_at = :archivedAt,
            updated_at = :updatedAt
        WHERE id = :taskId
        """
    )
    suspend fun archiveTask(
        taskId: Long,
        archivedAt: Date,
        updatedAt: Date
    )

    @Query(
        """
        UPDATE tasks
        SET completed_pomodoros = completed_pomodoros + 1,
            status = CASE
                WHEN status = 'todo' THEN 'in_progress'
                ELSE status
            END,
            updated_at = :updatedAt
        WHERE id = :taskId
        """
    )
    suspend fun recordCompletedFocusSession(
        taskId: Long,
        updatedAt: Date
    )
}
