package com.moa.pomodoroapps.Data

import androidx.room.*
import com.moa.pomodoroapps.Data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task")
    fun loadAllTask(): Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM Task")
    fun getTaskCount(): Flow<Int>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)


}