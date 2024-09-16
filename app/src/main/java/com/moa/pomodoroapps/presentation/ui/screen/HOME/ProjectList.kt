package com.moa.pomodoroapps.todo.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.moa.pomodoroapps.Data.Project

@Composable
fun ProjectList(
    projects: List<Project>,
    onClickRow: (Project) -> Unit,
    onClickDelete: (Project) -> Unit,
    onClickDone: (Project) -> Unit,
    onClickPlayPomo: (Project) -> Unit
) {
    LazyColumn {
        items(projects) { project ->
            ProjectRow(
                project = project,
                onClickRow = onClickRow,
                onClickDelete = onClickDelete,
                onClickDone = onClickDone,
                onClickPlayPomo = onClickPlayPomo
            )
        }
    }
}