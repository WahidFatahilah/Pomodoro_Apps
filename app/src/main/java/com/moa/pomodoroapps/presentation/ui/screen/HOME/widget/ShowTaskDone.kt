package com.moa.pomodoroapps.presentation.ui.screen.HOME.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.screen.HOME.MainViewModel
import com.moa.pomodoroapps.presentation.ui.theme.PinkSoft
import com.moa.pomodoroapps.presentation.ui.theme.backgroundBotton

@Composable
fun showTaskDone(
    viewModel: MainViewModel,
    tasks: List<Task>,
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 5.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = { viewModel.showTaskDone = false }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_undone),
                contentDescription = "Project",
                tint = MaterialTheme.colors.backgroundBotton,
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(id = R.string.Unfinished))
        }

        OutlinedButton(
            onClick = { viewModel.showTaskDone = true },
            enabled = false,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_done),
                contentDescription = "Project",
                tint = MaterialTheme.colors.PinkSoft,
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(id = R.string.finished))
        }


    }
    //Task Done
    TaskList(
        tasks = tasks.filter { it.isDone },
        onClickRow = {
            viewModel.setEditingTask(it)
            viewModel.isShowDialog = true
        },
        onClickDelete = { viewModel.deleteTask(it) },
        onClickPlayPomo = { task ->
            navController.navigate("pomodoro/${task}")
        },
        onClickDone = {
            viewModel.CheckBoxDone(it)
        }
    )
}
