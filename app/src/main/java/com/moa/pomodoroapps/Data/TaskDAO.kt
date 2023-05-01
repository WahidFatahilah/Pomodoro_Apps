package com.moa.pomodoroapps.Data

import androidx.room.*
import com.moa.pomodoroapps.Data.Task
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task")
    fun loadAllTask(): Flow<List<Task>>

    @Query("SELECT COUNT(*) FROM Task")
    fun getTaskCount(): Flow<Int>

    @Query("SELECT COUNT(isDone) FROM Task WHERE isDone = 1")
    fun getDoneTaskCount(): Flow<Int>

    @Query("SELECT * FROM Task WHERE deadline = :today")
    fun loadTasksDueToday(today: Date): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE deadline = :tomorrow")
    fun loadTasksDueTomorrow(tomorrow: Date): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE deadline BETWEEN :startOfWeek AND :endOfWeek")
    fun loadTasksDueThisWeek(startOfWeek: Date, endOfWeek: Date): Flow<List<Task>>


    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)


}