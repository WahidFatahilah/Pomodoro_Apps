package com.moa.pomodoroapps.todo.data

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.moa.pomodoroapps.todo.components.EditDialog
import com.moa.pomodoroapps.todo.components.TaskList
import com.moa.pomodoroapps.ui.theme.Magenta
import com.moa.pomodoroapps.ui.theme.Pink

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContent(viewModel: MainViewModel = hiltViewModel()) {

    if (viewModel.isShowDialog) {
        EditDialog()
    }
    Scaffold(
        floatingActionButton = {
        FloatingActionButton(
            onClick = { viewModel.isShowDialog = true },
            backgroundColor = MaterialTheme.colors.Pink,
            contentColor = MaterialTheme.colors.Magenta
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create New",
            )
        }
    },
        modifier = Modifier.padding(bottom = 64.dp)
    ) { it ->
        val tasks by viewModel.tasks.collectAsState(initial = emptyList())
        TaskList(
            tasks = tasks,
            onClickRow = {
                viewModel.setEditingTask(it)
                viewModel.isShowDialog = true
            },
            onClickDelete = { viewModel.deleteTask(it) }
        )
    }
}