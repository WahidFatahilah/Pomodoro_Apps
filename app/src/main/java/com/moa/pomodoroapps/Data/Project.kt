package com.moa.pomodoroapps.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Project(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var project: String,
    var title: String,
    var description: String,
    var isDone: Boolean,
    var deadline: Date?
    //var deadline: Date,
)


