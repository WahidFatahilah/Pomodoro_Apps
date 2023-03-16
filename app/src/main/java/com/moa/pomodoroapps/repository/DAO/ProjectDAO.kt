package com.moa.pomodoroapps.repository.DAO

import androidx.room.*
import com.moa.pomodoroapps.domain.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Projects")
    fun getAllProjects(): List<Project>

    @Query("SELECT * FROM Projects WHERE id = :projectId")
    fun getProjectById(projectId: Long): Project?

    @Insert
    fun insertProject(project: Project)

    @Update
    fun updateProject(project: Project)

    @Delete
    fun deleteProject(project: Project)
}
