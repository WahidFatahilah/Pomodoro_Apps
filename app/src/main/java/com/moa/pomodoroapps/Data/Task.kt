package com.moa.pomodoroapps.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String,
    //var deadline: Date,


)


