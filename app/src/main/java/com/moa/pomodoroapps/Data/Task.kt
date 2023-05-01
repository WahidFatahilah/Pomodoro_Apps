package com.moa.pomodoroapps.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var project: String,
    var title: String,
    var description: String,
    val isDone: Boolean,
    var deadline: Date?
)


