package com.xyz.pomotrack.presentation.ui.screen.HOME

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xyz.pomotrack.Data.FocusSessionDao
import com.xyz.pomotrack.Data.ProjectDao
import com.xyz.pomotrack.Data.Task
import com.xyz.pomotrack.Data.TaskDAO
import com.xyz.pomotrack.Data.TaskEntityDao
import com.xyz.pomotrack.Data.TaskStatus
import com.xyz.pomotrack.Data.TaskWithProjectRow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskDAO: TaskDAO,
    private val taskEntityDao: TaskEntityDao,
    private val projectDao: ProjectDao,
    private val focusSessionDao: FocusSessionDao
) : ViewModel() {

    var title by mutableStateOf("")
    var project by mutableStateOf("")
    var description by mutableStateOf("")
    var deadline by mutableStateOf(Date())
    var checkBox by mutableStateOf(false)
    var isShowDialog by mutableStateOf(false)
    var taskCreated by mutableStateOf(false)
    var showTaskDone by mutableStateOf(false)

    val tasks = taskDAO.loadAllTask().distinctUntilChanged()
    val homeTasks = taskEntityDao.getAllTasksWithProject().distinctUntilChanged()

    private var editingTask: Task? = null
    private var editingHomeTaskId: Long? = null
    private val zoneId = ZoneId.systemDefault()
    private val todayRange = dayRange(LocalDate.now(zoneId))
    private val tomorrowRange = dayRange(LocalDate.now(zoneId).plusDays(1))
    private val currentWeekRange = weekRange(LocalDate.now(zoneId))

    private val _taskCount = MutableStateFlow(0)
    val taskCount: StateFlow<Int> = _taskCount.asStateFlow()

    private val _taskCountDone = MutableStateFlow(0)
    val taskCountDone: StateFlow<Int> = _taskCountDone.asStateFlow()

    val projects = projectDao.getAllProjects().distinctUntilChanged()
    val todayProjectSummaries = projectDao
        .getProjectSummariesForDateRange(todayRange.first, todayRange.second)
        .distinctUntilChanged()
    val tomorrowProjectSummaries = projectDao
        .getProjectSummariesForDateRange(tomorrowRange.first, tomorrowRange.second)
        .distinctUntilChanged()
    val weekProjectSummaries = projectDao
        .getProjectSummariesForDateRange(currentWeekRange.first, currentWeekRange.second)
        .distinctUntilChanged()
    val todayProductivityStats = focusSessionDao
        .getPeriodProductivityStats(todayRange.first, todayRange.second)
        .distinctUntilChanged()
    val weekProductivityStats = focusSessionDao
        .getPeriodProductivityStats(currentWeekRange.first, currentWeekRange.second)
        .distinctUntilChanged()

    init {
        viewModelScope.launch {
            taskDAO.getTaskCount().collect {
                _taskCount.value = it
            }
        }

        viewModelScope.launch {
            taskDAO.getDoneTaskCount().collect {
                _taskCountDone.value = it
            }
        }
    }

    fun CheckBoxDone(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isDone = !task.isDone)
            taskDAO.updateTask(updatedTask)
        }
    }

    fun CheckBoxDone(task: TaskWithProjectRow) {
        viewModelScope.launch {
            val markDone = task.status != TaskStatus.DONE
            taskEntityDao.updateTaskStatus(
                taskId = task.taskId,
                status = if (markDone) TaskStatus.DONE else TaskStatus.TODO,
                completedAt = if (markDone) Date() else null,
                updatedAt = Date()
            )
        }
    }

    val isEditing: Boolean
        get() = editingTask != null || editingHomeTaskId != null

    fun setEditingTask(task: Task) {
        editingTask = task
        editingHomeTaskId = null
        title = task.title
        description = task.description
        deadline = task.deadline ?: Date()
    }

    fun setEditingTask(task: TaskWithProjectRow) {
        editingTask = null
        editingHomeTaskId = task.taskId
        title = task.title
        description = task.description.orEmpty()
        deadline = task.dueDate ?: Date()
    }

    fun createTask() {
        viewModelScope.launch {
            val newTask = Task(
                title = title,
                description = description,
                project = project,
                deadline = deadline,
                isDone = checkBox
            )
            taskDAO.insertTask(newTask)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDAO.deleteTask(task = task)
        }
    }

    fun deleteTask(task: TaskWithProjectRow) {
        viewModelScope.launch {
            val now = Date()
            taskEntityDao.archiveTask(
                taskId = task.taskId,
                archivedAt = now,
                updatedAt = now
            )
        }
    }

    fun updateTask() {
        val homeTaskId = editingHomeTaskId
        if (homeTaskId != null) {
            viewModelScope.launch {
                taskEntityDao.updateTaskContent(
                    taskId = homeTaskId,
                    title = title.trim(),
                    description = description.trim().ifBlank { null },
                    dueDate = deadline,
                    updatedAt = Date()
                )
                resetProperties()
            }
            return
        }

        editingTask?.let { task ->
            viewModelScope.launch {
                task.title = title
                task.description = description
                taskDAO.updateTask(task = task)
                resetProperties()
            }
        }
    }

    fun resetProperties() {
        editingTask = null
        editingHomeTaskId = null
        title = ""
        description = ""
        project = ""
        deadline = Date()
    }

    private fun dayRange(date: LocalDate): Pair<Date, Date> {
        val start = Date.from(date.atStartOfDay(zoneId).toInstant())
        val end = Date.from(date.plusDays(1).atStartOfDay(zoneId).toInstant())
        return start to end
    }

    private fun weekRange(date: LocalDate): Pair<Date, Date> {
        val startOfWeek = date.with(DayOfWeek.MONDAY)
        val endOfWeek = startOfWeek.plusDays(7)
        val start = Date.from(startOfWeek.atStartOfDay(zoneId).toInstant())
        val end = Date.from(endOfWeek.atStartOfDay(zoneId).toInstant())
        return start to end
    }
}
