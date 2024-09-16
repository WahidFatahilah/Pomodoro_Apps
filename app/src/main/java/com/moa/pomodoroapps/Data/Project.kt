package com.moa.pomodoroapps.Data

import androidx.room.*
import java.util.Date

/*
@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
)

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var description: String,
    var isDone: Boolean,
    var deadline: Date?
)
data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val tasks: List<Task>
)
*/


/*@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String
)*/
@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    //val tasks: List<Task> = listOf()
)


/*@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //var projectId: Int,
    var name: String,
    var description: String,
    var isDone: Boolean,
    var deadline: Date?
)*/

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var description: String,
    var isDone: Boolean,
    var deadline: Date?,
    var projectId: Int // foreign key column
)

data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val tasks: List<Task>
)


/*

data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val tasks: List<Task>
)
*/
