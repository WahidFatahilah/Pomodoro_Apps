package com.moa.pomodoroapps.presentation.ui.screen.Setting

import androidx.lifecycle.ViewModel
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.Data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingScreenViewModel @Inject constructor (private val taskDAO: TaskDAO) : ViewModel() {


}