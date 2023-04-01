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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val ProjectDAO: ProjectDAO): ViewModel() {
    var title by mutableStateOf("")
    var project by mutableStateOf("")
    var description by mutableStateOf("")
    var deadline by mutableStateOf(Date())
    var checkBox by mutableStateOf(false)
    var isShowDialog by mutableStateOf(false)
    var pomodoroTime = TimeUnit.MINUTES.toMillis(25)
    var isRunning = false

    val Projects = ProjectDAO.loadAllProject().distinctUntilChanged()
    private var editingProject: Project? = null

    fun CheckBoxDone(Project: Project) {
        viewModelScope.launch {
            val updatedProject = Project.copy(isDone = !Project.isDone)
            ProjectDAO.updateProject(updatedProject)
        }
    }

    val isEditing: Boolean
        get() = editingProject != null

    fun setEditingProject(Project: Project) {
        editingProject = Project
        title = Project.title
        description = Project.description

    }

    fun createProject() {
        viewModelScope.launch {

            var newProject = Project(title = title, description = description, project = project, deadline = deadline, isDone = checkBox)
            ProjectDAO.insertProject(newProject)
        }


    }

    fun deleteProject(Project: Project) {
        viewModelScope.launch {
            ProjectDAO.deleteProject(Project = Project)
        }
    }


    fun updateProject() {
        editingProject?.let { Project ->
            viewModelScope.launch {
                Project.title = title
                Project.description = description
                ProjectDAO.updateProject(Project = Project)
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