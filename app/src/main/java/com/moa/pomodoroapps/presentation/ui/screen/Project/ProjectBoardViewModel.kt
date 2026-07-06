package com.moa.pomodoroapps.presentation.ui.screen.Project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.pomodoroapps.Data.ProjectDao
import com.moa.pomodoroapps.Data.ProjectWithTasks
import com.moa.pomodoroapps.Data.TaskEntity
import com.moa.pomodoroapps.Data.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ProjectBoardViewModel @Inject constructor(
    private val projectDao: ProjectDao
) : ViewModel() {

    enum class DeadlineBucket {
        TODAY,
        TOMORROW,
        THIS_WEEK
    }

    private val zoneId = ZoneId.systemDefault()

    var selectedBucket by mutableStateOf(DeadlineBucket.TODAY)
        private set

    var selectedProjectId by mutableStateOf<Long?>(null)
        private set

    var isTaskSectionExpanded by mutableStateOf(true)
        private set

    var projectsWithTasks by mutableStateOf<List<ProjectWithTasks>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            projectDao.getAllProjectsWithTasks().collect { projects ->
                projectsWithTasks = projects
                syncSelectedProject(projects)
            }
        }
    }

    fun selectBucket(bucket: DeadlineBucket) {
        if (selectedBucket == bucket) return
        selectedBucket = bucket
        syncSelectedProject(projectsWithTasks)
    }

    fun selectProject(projectId: Long) {
        selectedProjectId = projectId
    }

    fun toggleTaskSectionExpanded() {
        isTaskSectionExpanded = !isTaskSectionExpanded
    }

    val filteredProjects: List<ProjectWithTasks>
        get() = projectsWithTasks.map(::filterAndSortProjectTasks)

    val selectedProject: ProjectWithTasks?
        get() {
            val visibleProjects = filteredProjects
            val selected = visibleProjects.firstOrNull { it.project.id == selectedProjectId }
            return selected ?: visibleProjects.firstOrNull()
        }

    val selectedProjectActiveTasks: List<TaskEntity>
        get() = selectedProject
            ?.tasks
            ?.filter { it.status != TaskStatus.DONE }
            ?.sortedWith(compareBy<TaskEntity> { it.sortOrder }.thenBy { it.createdAt })
            .orEmpty()

    val selectedProjectCompletedTasks: List<TaskEntity>
        get() = selectedProject
            ?.tasks
            ?.filter { it.status == TaskStatus.DONE }
            ?.sortedByDescending { it.completedAt ?: it.updatedAt }
            .orEmpty()

    val selectedProjectProgressLabel: String
        get() {
            val total = selectedProject?.tasks?.size ?: 0
            val completed = selectedProjectCompletedTasks.size
            return "$completed/$total"
        }

    private fun syncSelectedProject(projects: List<ProjectWithTasks>) {
        if (projects.isEmpty()) {
            selectedProjectId = null
            return
        }

        if (selectedProjectId == null || projects.none { it.project.id == selectedProjectId }) {
            selectedProjectId = projects.first().project.id
        }
    }

    private fun filterAndSortProjectTasks(projectWithTasks: ProjectWithTasks): ProjectWithTasks {
        return projectWithTasks.copy(
            tasks = projectWithTasks.tasks
                .filter(::matchesSelectedBucket)
                .sortedWith(
                    compareBy<TaskEntity> { it.status == TaskStatus.DONE }
                        .thenBy { it.sortOrder }
                        .thenBy { it.createdAt }
                )
        )
    }

    private fun matchesSelectedBucket(task: TaskEntity): Boolean {
        val dueDate = task.dueDate ?: return false
        val localDate = dueDate.toInstant().atZone(zoneId).toLocalDate()

        return when (selectedBucket) {
            DeadlineBucket.TODAY -> localDate == LocalDate.now(zoneId)
            DeadlineBucket.TOMORROW -> localDate == LocalDate.now(zoneId).plusDays(1)
            DeadlineBucket.THIS_WEEK -> {
                val today = LocalDate.now(zoneId)
                val startOfWeek = today.with(DayOfWeek.MONDAY)
                val endOfWeekExclusive = startOfWeek.plusDays(7)
                !localDate.isBefore(startOfWeek) && localDate.isBefore(endOfWeekExclusive)
            }
        }
    }
}
