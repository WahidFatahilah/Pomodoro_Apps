package com.moa.pomodoroapps.presentation.ui.screen.Pomodoro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.pomodoroapps.Data.FocusSessionDao
import com.moa.pomodoroapps.Data.FocusSessionEntity
import com.moa.pomodoroapps.Data.FocusSessionType
import com.moa.pomodoroapps.Data.TaskEntityDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskEntityDao: TaskEntityDao,
    private val focusSessionDao: FocusSessionDao
) : ViewModel() {

    private val taskId: Long = savedStateHandle.get<Long>("taskId") ?: -1L

    val taskDetail = taskEntityDao
        .getTaskWithProjectById(taskId)
        .distinctUntilChanged()

    var currentSessionId by mutableStateOf<Long?>(null)
        private set

    private var currentSessionType by mutableStateOf<FocusSessionType?>(null)
    var remainingTimeMillis by mutableStateOf(0L)
        private set

    var totalTimeMillis by mutableStateOf(0L)
        private set

    var isTimerRunning by mutableStateOf(false)
        private set

    var isTimerPaused by mutableStateOf(false)
        private set

    private var sessionStartedAtMillis by mutableStateOf<Long?>(null)
    private var pausedAtMillis by mutableStateOf<Long?>(null)
    private var accumulatedPauseMillis by mutableStateOf(0L)
    private var timerJob: Job? = null

    fun startTimer(sessionType: FocusSessionType, plannedMillis: Long) {
        if (plannedMillis <= 0L || currentSessionId != null) return

        viewModelScope.launch {
            val now = Date()
            currentSessionId = focusSessionDao.insertSession(
                FocusSessionEntity(
                    taskId = taskId,
                    sessionType = sessionType,
                    plannedMinutes = (plannedMillis / 60_000L).toInt().coerceAtLeast(1),
                    startedAt = now,
                    createdAt = now
                )
            )
            currentSessionType = sessionType
            totalTimeMillis = plannedMillis
            remainingTimeMillis = plannedMillis
            isTimerRunning = true
            isTimerPaused = false
            sessionStartedAtMillis = System.currentTimeMillis()
            pausedAtMillis = null
            accumulatedPauseMillis = 0L

            timerJob?.cancel()
            timerJob = launch {
                while (isTimerRunning && currentSessionId != null) {
                    if (!isTimerPaused) {
                        remainingTimeMillis = calculateRemainingMillis(System.currentTimeMillis())
                        if (remainingTimeMillis <= 0L) {
                            remainingTimeMillis = 0L
                            isTimerRunning = false
                            isTimerPaused = false
                            completeCurrentSession(totalTimeMillis)
                            return@launch
                        }
                    }
                    delay(1000L)
                }
            }
        }
    }

    fun pauseTimer() {
        if (!isTimerRunning || isTimerPaused) return
        isTimerPaused = true
        pausedAtMillis = System.currentTimeMillis()
    }

    fun resumeTimer() {
        val pausedAt = pausedAtMillis ?: return
        accumulatedPauseMillis += System.currentTimeMillis() - pausedAt
        pausedAtMillis = null
        isTimerPaused = false
        remainingTimeMillis = calculateRemainingMillis(System.currentTimeMillis())
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        isTimerRunning = false
        isTimerPaused = false
        remainingTimeMillis = 0L
        totalTimeMillis = 0L
        sessionStartedAtMillis = null
        pausedAtMillis = null
        accumulatedPauseMillis = 0L
    }

    fun completeCurrentSession(actualMillis: Long) {
        val sessionId = currentSessionId ?: return
        val sessionType = currentSessionType ?: return

        viewModelScope.launch {
            focusSessionDao.updateSessionCompletion(
                sessionId = sessionId,
                actualSeconds = (actualMillis / 1000L).coerceAtLeast(0L),
                endedAt = Date(),
                isCompleted = true
            )

            if (sessionType == FocusSessionType.FOCUS) {
                taskEntityDao.recordCompletedFocusSession(
                    taskId = taskId,
                    updatedAt = Date()
                )
            }

            clearActiveSession()
            stopTimer()
        }
    }

    fun updateCurrentSessionProgress(actualMillis: Long) {
        val sessionId = currentSessionId ?: return

        viewModelScope.launch {
            focusSessionDao.updateSessionProgress(
                sessionId = sessionId,
                actualSeconds = (actualMillis / 1000L).coerceAtLeast(0L)
            )
        }
    }

    fun cancelCurrentSession(actualMillis: Long) {
        val sessionId = currentSessionId ?: return

        viewModelScope.launch {
            focusSessionDao.updateSessionCompletion(
                sessionId = sessionId,
                actualSeconds = (actualMillis / 1000L).coerceAtLeast(0L),
                endedAt = Date(),
                isCompleted = false
            )
            clearActiveSession()
            stopTimer()
        }
    }

    private fun clearActiveSession() {
        currentSessionId = null
        currentSessionType = null
    }

    private fun calculateRemainingMillis(nowMillis: Long): Long {
        val startedAt = sessionStartedAtMillis ?: return remainingTimeMillis
        val elapsed = (nowMillis - startedAt - accumulatedPauseMillis).coerceAtLeast(0L)
        return (totalTimeMillis - elapsed).coerceAtLeast(0L)
    }
}
