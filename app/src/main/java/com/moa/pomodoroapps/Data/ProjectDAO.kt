package com.moa.pomodoroapps.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
@Dao
interface ProjectDAO {
    @Insert
    suspend fun insertProject(project: Project): Long

    @Insert
    suspend fun insertTask(task: Task): Long


    @Transaction
    @Query("SELECT * FROM Project")
    fun getAllProjectsWithTasks(): Flow<List<ProjectWithTasks>>

    @Transaction
    @Query("SELECT * FROM Project WHERE id = :projectId")
    fun getProjectWithTasks(projectId: Int): Flow<ProjectWithTasks>

    @Update
    suspend fun updateProject(project: Project)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteProject(project: Project)

    @Delete
    suspend fun deleteTask(task: Task)
}

*/



@Dao
interface ProjectDAO {
    @Insert
    suspend fun insertProject(project: Project): Long

    @Insert
    suspend fun insertTask(task: Task): Long

/*    @Transaction
    @Query("SELECT * FROM Project")
    fun getAllProjectsWithTasks(): Flow<List<Project>>*/

/*    @Transaction
    @Query("SELECT * FROM Project WHERE id = :projectId")
    fun getProjectWithTasks(projectId: Int): Flow<Project>*/

    @Transaction
    @Query("SELECT * FROM Project")
    fun getProjectsWithTasks(): List<Project>

    @Transaction
    @Query("SELECT * FROM Project")
    fun getAllProjectsWithTasks(): Flow<List<ProjectWithTasks>>


    @Transaction
    @Query("SELECT * FROM Project WHERE id = :projectId")
    fun getProjectWithTasks(projectId: Int): Flow<Project>

    @Update
    suspend fun updateProject(project: Project)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteProject(project: Project)

    @Delete
    suspend fun deleteTask(task: Task)
}
