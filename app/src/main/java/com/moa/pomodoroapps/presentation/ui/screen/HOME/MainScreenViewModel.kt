package com.moa.pomodoroapps

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.pomodoroapps.Data.Project
import com.moa.pomodoroapps.Data.ProjectDAO
import com.moa.pomodoroapps.Data.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val ProjectDAO: ProjectDAO): ViewModel() {
    var title by mutableStateOf("")
    var isAddingTask by mutableStateOf(false)
    var projectName by mutableStateOf("")
    //var project by mutableStateOf("")
    var description by mutableStateOf("")
    var deadline by mutableStateOf(Date())
    var checkBox by mutableStateOf(false)
    var isShowDialog by mutableStateOf(false)
    var pomodoroTime = TimeUnit.MINUTES.toMillis(25)
    var isRunning = false
    var taskTitle: String by mutableStateOf("")
    var taskDescription: String by mutableStateOf("")
    var taskDeadline: LocalDate by mutableStateOf(LocalDate.now())


    /*val Projects = ProjectDAO.getAllProjectsWithTasks().distinctUntilChanged()*/
    val Projects = ProjectDAO.getAllProjectsWithTasks().distinctUntilChanged()
    private var editingProject: Project? = null

/*    fun CheckBoxDone(Project: Project) {
        viewModelScope.launch {
            val updatedProject = Project.copy(isDone = !Project.isDone)
            ProjectDAO.updateProject(updatedProject)
        }
    }*/

    val isEditing: Boolean
        get() = editingProject != null

    fun setEditingProject(Project: Project) {
        editingProject = Project
        title = Project.name
    }

    fun createProjectWithTask() {
        viewModelScope.launch {
            val newProject = Project(
                name = projectName,
            )
            val projectId = ProjectDAO.insertProject(newProject)

            val newTask = Task(
                projectId = projectId.toInt(),
                name = taskTitle,
                description = taskDescription,
                deadline = Date.from(taskDeadline.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                isDone = false
            )
            ProjectDAO.insertTask(newTask)

            resetProperties()
        }
    }

    fun deleteProject(Project: Project) {
        viewModelScope.launch {
            ProjectDAO.deleteProject(project = Project)
        }
    }


    fun updateProject() {
        editingProject?.let { Project ->
            viewModelScope.launch {
                //Project.title = title
                //Project.description = description
                //ProjectDAO.updateProject(Project = Project)
            }
        }
    }

    fun resetProperties(){
        editingProject = null
        title = ""
        description = ""
    }
    fun startPomodoro() {
        isRunning = true
    }

    fun stopPomodoro() {
        isRunning = false
    }

}