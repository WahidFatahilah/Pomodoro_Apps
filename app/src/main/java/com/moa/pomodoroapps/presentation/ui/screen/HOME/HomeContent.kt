package com.moa.pomodoroapps.presentation.ui.screen.HOME

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.navigation.topnav.CustomTopAppBar
import com.moa.pomodoroapps.presentation.ui.screen.HOME.widget.EmptyTaskAndProject
import com.moa.pomodoroapps.presentation.ui.screen.HOME.widget.Statistic
import com.moa.pomodoroapps.presentation.ui.screen.HOME.widget.showTaskDone
import com.moa.pomodoroapps.presentation.ui.theme.*
import com.moa.pomodoroapps.todo.components.EditDialog
import com.moa.pomodoroapps.presentation.ui.screen.HOME.widget.TaskList
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeContent(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    if (viewModel.isShowDialog) {
        EditDialog()
    }

    val totalTask = tasks.count()
    val totalTaskDone = tasks.filter { it.isDone }.count()

    var percentage by remember {
        mutableStateOf(0.0)
    }
    percentage = ((totalTaskDone).toDouble() / (totalTask.toDouble())) * 100.0
    var totalPercentage = percentage.toInt()


    Column(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxSize()
            ,

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    CustomTopAppBar()

                    Statistic(totalTask, totalTaskDone, totalPercentage)

                    Text(
                        text = "Project List",
                        style = Heading_H1,
                        color = MaterialTheme.colors.FontColor
                    )

                    Divider(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.click_to_edit),
                        style = Ket_1,
                        color = MaterialTheme.colors.FontColor
                    )


                    if(!viewModel.showTaskDone) {
                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 5.dp)
                                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(
                                onClick = { viewModel.showTaskDone = false },
                                enabled = false,
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_undone) ,
                                    contentDescription = stringResource(id = R.string.task),
                                    tint = MaterialTheme.colors.PinkSoft,
                                )
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text(text = stringResource(R.string.Unfinished))
                            }

                            OutlinedButton(
                                onClick = { viewModel.showTaskDone = true }
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_done) ,
                                    contentDescription = "Project",
                                    tint = MaterialTheme.colors.backgroundBotton,
                                )
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text(text = "Finished")
                            }


                        }

                        if (tasks.isNotEmpty()) {
                            if(totalPercentage == 100){
                                EmptyTaskAndProject()
                            }
                            else{
                                TaskList(
                                    tasks = tasks.filter { !it.isDone },
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

                        } else {
                            EmptyTaskAndProject()
                        }


                    }

                    if(viewModel.showTaskDone) {
                        showTaskDone(viewModel, tasks, navController)

                    }





                }
            }
        }




@Composable
fun TaskList(
    tasks: List<Task>,
    viewModel: MainViewModel,
    navController: NavController,
) {
    TaskList(
        tasks = tasks,
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
