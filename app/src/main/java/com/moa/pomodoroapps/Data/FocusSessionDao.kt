package com.moa.pomodoroapps.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface FocusSessionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Update
    suspend fun updateSession(session: FocusSessionEntity)

    @Query(
        """
        UPDATE focus_sessions
        SET actual_seconds = :actualSeconds,
            ended_at = :endedAt,
            is_completed = :isCompleted
        WHERE id = :sessionId
        """
    )
    suspend fun updateSessionCompletion(
        sessionId: Long,
        actualSeconds: Long,
        endedAt: Date?,
        isCompleted: Boolean
    )

    @Query(
        """
        UPDATE focus_sessions
        SET actual_seconds = :actualSeconds
        WHERE id = :sessionId
          AND ended_at IS NULL
        """
    )
    suspend fun updateSessionProgress(
        sessionId: Long,
        actualSeconds: Long
    )

    @Query(
        """
        SELECT * FROM focus_sessions
        WHERE task_id = :taskId
        ORDER BY started_at DESC
        """
    )
    fun getSessionsForTask(taskId: Long): Flow<List<FocusSessionEntity>>

    @Query(
        """
        SELECT COALESCE(SUM(actual_seconds), 0)
        FROM focus_sessions
        WHERE session_type = 'focus'
          AND started_at >= :start
          AND started_at < :end
          AND is_completed = 1
        """
    )
    fun getFocusSecondsByDateRange(start: Date, end: Date): Flow<Long>

    @Query(
        """
        SELECT COUNT(*)
        FROM focus_sessions
        WHERE session_type = 'focus'
          AND started_at >= :start
          AND started_at < :end
          AND is_completed = 1
        """
    )
    fun getCompletedFocusSessionsByDateRange(start: Date, end: Date): Flow<Int>

    @Query(
        """
        SELECT
            strftime('%w', started_at / 1000, 'unixepoch', 'localtime') AS day_of_week,
            COALESCE(SUM(actual_seconds), 0) AS total_focus_seconds
        FROM focus_sessions
        WHERE session_type = 'focus'
          AND started_at >= :start
          AND started_at < :end
          AND is_completed = 1
        GROUP BY day_of_week
        ORDER BY day_of_week ASC
        """
    )
    fun getWeeklyFocusChart(start: Date, end: Date): Flow<List<WeeklyFocusChartRow>>

    @Query(
        """
        SELECT
            t.id AS task_id,
            t.title AS task_title,
            p.id AS project_id,
            p.name AS project_name,
            COALESCE(SUM(fs.actual_seconds), 0) AS total_focus_seconds
        FROM focus_sessions fs
        INNER JOIN tasks t ON t.id = fs.task_id
        INNER JOIN projects p ON p.id = t.project_id
        WHERE fs.session_type = 'focus'
          AND fs.started_at >= :start
          AND fs.started_at < :end
          AND fs.is_completed = 1
        GROUP BY t.id, t.title, p.id, p.name
        ORDER BY total_focus_seconds DESC
        """
    )
    fun getTopTasksByFocusTime(start: Date, end: Date): Flow<List<TopTaskFocusRow>>

    @Query(
        """
        SELECT
            (
                SELECT COUNT(*)
                FROM tasks t
                WHERE t.completed_at >= :start
                  AND t.completed_at < :end
                  AND t.status = 'done'
            ) AS completed_tasks,
            (
                SELECT COUNT(*)
                FROM focus_sessions fs
                WHERE fs.started_at >= :start
                  AND fs.started_at < :end
                  AND fs.session_type = 'focus'
                  AND fs.is_completed = 1
            ) AS total_focus_sessions,
            (
                SELECT COALESCE(SUM(fs.actual_seconds), 0)
                FROM focus_sessions fs
                WHERE fs.started_at >= :start
                  AND fs.started_at < :end
                  AND fs.session_type = 'focus'
            ) AS total_focus_seconds,
            (
                SELECT COUNT(DISTINCT t.project_id)
                FROM tasks t
                WHERE t.due_date >= :start
                  AND t.due_date < :end
                  AND t.archived_at IS NULL
            ) AS active_projects
        """
    )
    fun getPeriodProductivityStats(start: Date, end: Date): Flow<PeriodProductivityStatsRow>
}
