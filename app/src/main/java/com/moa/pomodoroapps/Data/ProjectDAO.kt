package com.moa.pomodoroapps.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDAO {
    @Insert
    suspend fun insertProject(Project: Project)

    @Query("SELECT * FROM Project")
    fun loadAllProject(): Flow<List<Project>>

    @Update
    suspend fun updateProject(Project: Project)

    @Delete
    suspend fun deleteProject(Project: Project)


}