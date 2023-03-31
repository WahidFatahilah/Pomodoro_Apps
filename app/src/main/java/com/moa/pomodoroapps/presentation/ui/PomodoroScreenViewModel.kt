package com.moa.pomodoroapps.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.moa.pomodoroapps.Data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PomodoroScreenViewModel @Inject constructor(): ViewModel(){

    var durationPomodoro by mutableStateOf(15L)
    var durationShortBreak by mutableStateOf(5L)
    var durationLongBreak by mutableStateOf(10L)
    var namaKegiatan by mutableStateOf("")
    var remainingTimePomodoro by  mutableStateOf(10L)
    var remainingTimeShortBreak by mutableStateOf(10L)
    var remainingTimeLongBreak by mutableStateOf(10L)
    var isRunning by  mutableStateOf(mutableStateOf(false))
    var isPaused by  mutableStateOf(mutableStateOf(false))
    var showTimerDialog by  mutableStateOf(false)
    val nameList = listOf("Mengerjakan Pr", "Membaca Buku", "Mendengar Podcast",)
    var selectedName by  mutableStateOf(nameList[0])
    var showDialog by mutableStateOf(false)
    var sessionCount by  mutableStateOf(0)
    var isPomodoro by  mutableStateOf(true)
    var isShortBreak by  mutableStateOf(false)
    var isLongBreak by  mutableStateOf(false)

}