package com.moa.pomodoroapps.repository.DAO

import androidx.room.*
import com.moa.pomodoroapps.domain.Break

@Dao
interface BreakDao {

    @Query("SELECT * FROM Breaks")
    fun getAllBreaks(): List<Break>

    @Query("SELECT * FROM Breaks WHERE id = :breakId")
    fun getBreakById(breakId: Long): Break?

    @Insert
    fun insertBreak(breakInsert : Break)

    @Update
    fun updateBreak(breakUpdate: Break)

    @Delete
    fun deleteBreak(breakDelete : Break)
}