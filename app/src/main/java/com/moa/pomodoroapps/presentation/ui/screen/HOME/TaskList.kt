package com.moa.pomodoroapps.todo.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moa.pomodoroapps.Data.Task

@Composable
fun TaskList(
    tasks: List<Task>,
    onClickRow: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
    onClickDone: (Task) -> Unit,
    onClickPlayPomo: (Task) -> Unit,
) {
    LazyColumn(){
        items(tasks){ task ->
            TaskRow(task = task, onClickRow = onClickRow, onClickDelete = onClickDelete, onClickPlayPomo = onClickPlayPomo, onClickDone = onClickDone)
        }
    }
}