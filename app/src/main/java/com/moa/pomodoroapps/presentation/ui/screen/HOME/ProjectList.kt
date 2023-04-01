package com.moa.pomodoroapps.todo.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.moa.pomodoroapps.Data.Project

@Composable
fun ProjectList(
    Projects: List<Project>,
    onClickRow: (Project) -> Unit,
    onClickDelete: (Project) -> Unit,
    onClickDone: (Project) -> Unit,
    onClickPlayPomo: (Project) -> Unit,
) {
    LazyColumn{
        items(Projects){ Project ->
            ProjectRow(Project = Project, onClickRow = onClickRow, onClickDelete = onClickDelete, onClickPlayPomo = onClickPlayPomo, onClickDone = onClickDone)
        }
    }
}