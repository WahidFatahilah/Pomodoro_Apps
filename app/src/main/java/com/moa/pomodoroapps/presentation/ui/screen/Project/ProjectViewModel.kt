package com.moa.pomodoroapps.presentation.ui.screen.Project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.moa.pomodoroapps.Data.AppDatabase
import com.moa.pomodoroapps.Data.ProjectDao
import com.moa.pomodoroapps.Data.ProjectEntity
import com.moa.pomodoroapps.Data.TaskEntity
import com.moa.pomodoroapps.Data.TaskEntityDao
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

private const val TAG = "ProjectViewModel"

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val appDatabase: AppDatabase,
    private val projectDao: ProjectDao,
    private val taskEntityDao: TaskEntityDao
) : ViewModel() {

    data class TaskDraft(
        val id: Long,
        val title: String = ""
    )

    private val zoneId = ZoneId.systemDefault()
    private var nextTaskDraftId = 0L

    var selectedDate by mutableStateOf(LocalDate.now(zoneId))
        private set

    var useExistingProject by mutableStateOf(false)
        private set

    var projectName by mutableStateOf("")
        private set

    var projectSearch by mutableStateOf("")
        private set

    var selectedProjectId by mutableStateOf<Long?>(null)
        private set

    var existingProjects by mutableStateOf<List<ProjectEntity>>(emptyList())
        private set

    val taskDrafts = mutableStateListOf(newTaskDraft())

    var isSaving by mutableStateOf(false)
        private set

    var showSuccessBanner by mutableStateOf(false)
        private set

    var userMessage by mutableStateOf<String?>(null)
        private set

    val filteredProjects: List<ProjectEntity>
        get() {
            val query = projectSearch.trim()
            return if (query.isBlank()) {
                existingProjects.take(6)
            } else {
                existingProjects
                    .filter { it.name.contains(query, ignoreCase = true) }
                    .take(6)
            }
        }

    val selectedProjectName: String?
        get() = existingProjects.firstOrNull { it.id == selectedProjectId }?.name

    val filledTaskCount: Int
        get() = taskDrafts.count { it.title.isNotBlank() }

    val canSave: Boolean
        get() = resolvedProjectName().isNotBlank() && filledTaskCount > 0 && !isSaving

    init {
        Log.d(TAG, "init - starting getAllProjects Flow collection")
        viewModelScope.launch {
            projectDao.getAllProjects().collect { projects ->
                Log.d(TAG, "init - getAllProjects emitted ${projects.size} projects")
                existingProjects = projects
            }
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        selectedDate = date
    }

    fun toggleExistingProject(enabled: Boolean) {
        useExistingProject = enabled
        if (enabled) {
            projectName = ""
        } else {
            projectSearch = ""
            selectedProjectId = null
        }
    }

    fun updateProjectName(value: String) {
        projectName = value
    }

    fun updateProjectSearch(value: String) {
        projectSearch = value
        selectedProjectId = findLocalProjectByName(value)?.id
    }

    fun selectExistingProject(project: ProjectEntity) {
        selectedProjectId = project.id
        projectSearch = project.name
    }

    fun addTaskDraft() {
        taskDrafts.add(newTaskDraft())
    }

    fun updateTaskDraft(taskDraftId: Long, value: String) {
        val index = taskDrafts.indexOfFirst { it.id == taskDraftId }
        if (index != -1) {
            taskDrafts[index] = taskDrafts[index].copy(title = value)
        }
    }

    fun removeTaskDraft(taskDraftId: Long) {
        if (taskDrafts.size == 1) {
            taskDrafts[0] = taskDrafts[0].copy(title = "")
            return
        }
        taskDrafts.removeAll { it.id == taskDraftId }
    }

    fun clearForm() {
        selectedDate = LocalDate.now(zoneId)
        useExistingProject = false
        projectName = ""
        projectSearch = ""
        selectedProjectId = null
        showSuccessBanner = false
        userMessage = null
        taskDrafts.clear()
        taskDrafts.add(newTaskDraft())
    }

    fun dismissSuccessBanner() {
        showSuccessBanner = false
    }

    fun consumeUserMessage() {
        userMessage = null
    }

    fun saveProjectPlan() {
        Log.d(TAG, "saveProjectPlan() called, isSaving=$isSaving")
        if (isSaving) {
            Log.w(TAG, "saveProjectPlan() skipped - already saving")
            return
        }

        val trimmedTasks = taskDrafts
            .map { it.title.trim() }
            .filter { it.isNotBlank() }

        Log.d(TAG, "saveProjectPlan() trimmedTasks.size=${trimmedTasks.size}, taskDrafts.size=${taskDrafts.size}")

        if (trimmedTasks.isEmpty()) {
            Log.w(TAG, "saveProjectPlan() aborted - no tasks")
            userMessage = "Tambahkan minimal satu tugas."
            return
        }

        val resolvedProjectName = resolvedProjectName()
        Log.d(TAG, "saveProjectPlan() resolvedProjectName='$resolvedProjectName', useExistingProject=$useExistingProject, selectedProjectId=$selectedProjectId")
        if (resolvedProjectName.isBlank()) {
            Log.w(TAG, "saveProjectPlan() aborted - project name blank")
            userMessage = if (useExistingProject) {
                "Pilih project yang sudah ada."
            } else {
                "Isi nama project terlebih dahulu."
            }
            return
        }

        viewModelScope.launch {
            isSaving = true
            Log.d(TAG, "saveProjectPlan() coroutine started, isSaving=true")

            try {
                val now = Date()
                val dueDate = Date.from(selectedDate.atStartOfDay(zoneId).toInstant())
                Log.d(TAG, "saveProjectPlan() entering withTransaction, now=$now, dueDate=$dueDate")

                appDatabase.withTransaction {
                    Log.d(TAG, "saveProjectPlan() inside withTransaction - calling resolveProjectIdForSave")
                    val projectId = resolveProjectIdForSave(now)
                    Log.d(TAG, "saveProjectPlan() resolved projectId=$projectId, building ${trimmedTasks.size} task entities")

                    val taskEntities = trimmedTasks.mapIndexed { index, title ->
                        TaskEntity(
                            projectId = projectId,
                            title = title,
                            dueDate = dueDate,
                            sortOrder = index,
                            createdAt = now,
                            updatedAt = now
                        )
                    }

                    Log.d(TAG, "saveProjectPlan() calling taskEntityDao.insertTasks with ${taskEntities.size} tasks")
                    val insertedIds = taskEntityDao.insertTasks(taskEntities)
                    Log.d(TAG, "saveProjectPlan() insertTasks completed, insertedIds=$insertedIds")
                }

                Log.d(TAG, "saveProjectPlan() withTransaction completed successfully")
                clearForm()
                showSuccessBanner = true
                Log.d(TAG, "saveProjectPlan() form cleared, success banner shown")
            } catch (exception: IllegalStateException) {
                Log.e(TAG, "saveProjectPlan() IllegalStateException: ${exception.message}", exception)
                userMessage = exception.message ?: "Data project belum valid."
            } catch (exception: Exception) {
                Log.e(TAG, "saveProjectPlan() Exception: ${exception.message}", exception)
                userMessage = exception.message ?: "Gagal menyimpan data project."
            } finally {
                isSaving = false
                Log.d(TAG, "saveProjectPlan() finally block, isSaving=false")
            }
        }
    }

    private suspend fun resolveProjectIdForSave(now: Date): Long {
        Log.d(TAG, "resolveProjectIdForSave() useExistingProject=$useExistingProject, selectedProjectId=$selectedProjectId")
        return if (useExistingProject) {
            Log.d(TAG, "resolveProjectIdForSave() looking up existing project by id=$selectedProjectId")
            val activeProject = selectedProjectId?.let {
                Log.d(TAG, "resolveProjectIdForSave() calling getActiveProjectById($it)")
                projectDao.getActiveProjectById(it)
            } ?: run {
                val name = projectSearch.trim()
                Log.d(TAG, "resolveProjectIdForSave() project by id not found, calling getActiveProjectByName('$name')")
                projectDao.getActiveProjectByName(name)
            }
            Log.d(TAG, "resolveProjectIdForSave() activeProject=${activeProject?.let { "id=${it.id}, name='${it.name}'" } ?: "null"}")
            if (activeProject == null) {
                Log.e(TAG, "resolveProjectIdForSave() throwing - project not found")
                throw IllegalStateException("Project tidak ditemukan. Pilih project yang tersedia.")
            }

            Log.d(TAG, "resolveProjectIdForSave() calling updateProject for id=${activeProject.id}")
            projectDao.updateProject(activeProject.copy(updatedAt = now))
            Log.d(TAG, "resolveProjectIdForSave() updateProject completed, returning id=${activeProject.id}")
            activeProject.id
        } else {
            val trimmedProjectName = projectName.trim()
            Log.d(TAG, "resolveProjectIdForSave() creating new project, name='$trimmedProjectName'")
            Log.d(TAG, "resolveProjectIdForSave() calling getActiveProjectByName('$trimmedProjectName') to check for duplicates")
            val existingProject = projectDao.getActiveProjectByName(trimmedProjectName)
            if (existingProject != null) {
                Log.e(TAG, "resolveProjectIdForSave() throwing - project name already exists: id=${existingProject.id}")
                throw IllegalStateException("Nama project sudah ada. Aktifkan opsi project sudah ada untuk memakainya.")
            }

            Log.d(TAG, "resolveProjectIdForSave() calling insertProject")
            val newId = projectDao.insertProject(
                ProjectEntity(
                    name = trimmedProjectName,
                    createdAt = now,
                    updatedAt = now
                )
            )
            Log.d(TAG, "resolveProjectIdForSave() insertProject completed, newId=$newId")
            newId
        }
    }

    private fun resolvedProjectName(): String {
        return if (useExistingProject) {
            projectSearch.trim()
        } else {
            projectName.trim()
        }
    }

    private fun findLocalProjectByName(name: String): ProjectEntity? {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) return null

        return existingProjects.firstOrNull {
            it.name.equals(trimmedName, ignoreCase = true)
        }
    }

    private fun newTaskDraft(): TaskDraft {
        nextTaskDraftId += 1
        return TaskDraft(id = nextTaskDraftId)
    }
}
