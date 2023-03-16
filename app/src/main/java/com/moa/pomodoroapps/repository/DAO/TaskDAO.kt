package com.moa.pomodoroapps.repository.DAO

import androidx.room.*
import com.moa.pomodoroapps.domain.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM Tasks")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    fun getTaskById(taskId: Long): Task?

    @Insert
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}
