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
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query(
        """
        SELECT * FROM projects
        WHERE archived_at IS NULL
        ORDER BY updated_at DESC
        """
    )
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query(
        """
        SELECT * FROM projects
        WHERE archived_at IS NULL
          AND name LIKE '%' || :query || '%'
        ORDER BY name ASC
        """
    )
    fun searchProjects(query: String): Flow<List<ProjectEntity>>

    @Query(
        """
        SELECT * FROM projects
        WHERE id = :projectId
        LIMIT 1
        """
    )
    fun getProjectById(projectId: Long): Flow<ProjectEntity?>

    @Query(
        """
        SELECT * FROM projects
        WHERE id = :projectId
          AND archived_at IS NULL
        LIMIT 1
        """
    )
    suspend fun getActiveProjectById(projectId: Long): ProjectEntity?

    @Query(
        """
        SELECT * FROM projects
        WHERE LOWER(name) = LOWER(:name)
          AND archived_at IS NULL
        LIMIT 1
        """
    )
    suspend fun getActiveProjectByName(name: String): ProjectEntity?

    @Transaction
    @Query(
        """
        SELECT * FROM projects
        WHERE id = :projectId
        LIMIT 1
        """
    )
    fun getProjectWithTasks(projectId: Long): Flow<ProjectWithTasks?>

    @Transaction
    @Query(
        """
        SELECT * FROM projects
        WHERE archived_at IS NULL
        ORDER BY updated_at DESC
        """
    )
    fun getAllProjectsWithTasks(): Flow<List<ProjectWithTasks>>

    @Query(
        """
        SELECT
            p.id AS project_id,
            p.name AS project_name,
            COUNT(t.id) AS total_tasks,
            SUM(CASE WHEN t.status = 'done' THEN 1 ELSE 0 END) AS completed_tasks
        FROM projects p
        LEFT JOIN tasks t
            ON t.project_id = p.id
           AND t.archived_at IS NULL
           AND (:start IS NULL OR t.due_date >= :start)
           AND (:end IS NULL OR t.due_date < :end)
        WHERE p.archived_at IS NULL
        GROUP BY p.id, p.name
        ORDER BY p.updated_at DESC
        """
    )
    fun getProjectSummariesForDateRange(
        start: Date?,
        end: Date?
    ): Flow<List<ProjectSummaryRow>>
}
