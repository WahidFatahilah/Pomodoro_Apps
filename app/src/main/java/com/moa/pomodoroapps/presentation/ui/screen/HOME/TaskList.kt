package com.moa.pomodoroapps.presentation.ui.screen.HOME

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.moa.pomodoroapps.Data.Project
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.todo.components.ProjectRow

@Composable
fun TaskList(
    Task: List<Task>,
    onClickTaskRow: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
    onClickTaskDone: (Task) -> Unit,
    onClickTaskPlayPomo: (Task) -> Unit,
) {

    LazyColumn{
        items(Task){ Task ->
            TaskRow(Task = Task, onClickTaskRow = onClickTaskRow, onClickTaskDelete = onClickDelete, onClickTaskPlayPomo = onClickTaskPlayPomo, onClickTaskDone = onClickTaskDone)
        }
    }
}