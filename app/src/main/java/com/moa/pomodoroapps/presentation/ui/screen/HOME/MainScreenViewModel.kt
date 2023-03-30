package com.moa.pomodoroapps

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.Data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val taskDAO: TaskDAO): ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var isShowDialog by mutableStateOf(false)
    var pomodoroTime = TimeUnit.MINUTES.toMillis(25)
    var isRunning = false

    val tasks = taskDAO.loadAllTask().distinctUntilChanged()

    private var editingTask: Task? = null
    val isEditing: Boolean
        get() = editingTask != null

    fun setEditingTask(task: Task) {
        editingTask = task
        title = task.title
        description = task.description

    }

    fun createTask() {
        viewModelScope.launch {
            var newTask = Task(title = title, description = description)
            taskDAO.insertTask(newTask)

        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDAO.deleteTask(task = task)
        }
    }

    fun updateTask() {
        editingTask?.let { task ->
            viewModelScope.launch {
                task.title = title
                task.description = description
                taskDAO.updateTask(task = task)
            }
        }
    }

    fun resetProperties(){
        editingTask = null
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