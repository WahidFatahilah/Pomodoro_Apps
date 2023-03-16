package com.moa.pomodoroapps.repository.DAO

import androidx.room.*
import com.moa.pomodoroapps.domain.Pomodoro

@Dao
interface PomodoroDao {
    @Query("SELECT * FROM Pomodoros")
    fun getAllPomodoros(): List<Pomodoro>

    @Query("SELECT * FROM Pomodoros WHERE id = :pomodoroId")
    fun getPomodoroById(pomodoroId: Long): Pomodoro?

    @Insert
    fun insertPomodoro(pomodoro: Pomodoro)

    @Update
    fun updatePomodoro(pomodoro: Pomodoro)

    @Delete
    fun deletePomodoro(pomodoro: Pomodoro)
}