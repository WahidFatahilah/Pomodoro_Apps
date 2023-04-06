package com.moa.pomodoroapps.presentation.ui.screen.HOME

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adidongs.cobapomodoro.ui.feature.appbarcustom.CardHome
import com.moa.pomodoroapps.MainViewModel
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.moa.pomodoroapps.R

import com.moa.pomodoroapps.presentation.navigation.bottomnav.BottomBarScreen.Home.title
import com.moa.pomodoroapps.presentation.navigation.topnav.CustomTopAppBar
import com.moa.pomodoroapps.presentation.ui.screen.Pomodoro.PomodoroScreen
import com.moa.pomodoroapps.presentation.ui.theme.*
import com.moa.pomodoroapps.todo.components.EditDialog
import com.moa.pomodoroapps.todo.components.TaskList


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeContent(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {
    var navController = rememberNavController()
    val isStartPomodoro by viewModel.isStartPomodoro.observeAsState(initial = true)
    val taskCountState by viewModel.taskCount.collectAsState()
    val taskCountDoneState by viewModel.taskCount.collectAsState()

    if (viewModel.isShowDialog) {
        EditDialog()
    }



    Column(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxSize(),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val tasks by viewModel.tasks.collectAsState(initial = emptyList())


        NavHost(navController, startDestination = "tasks") {
            composable("tasks") {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    CustomTopAppBar()
                    CardHome(totalProject = taskCountState, totalProjectDone = taskCountDoneState)

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
                        text = "Click To Edit Project",
                        style = Ket_1,
                        color = MaterialTheme.colors.FontColor
                    )

                    if (tasks.isNotEmpty()) {
                        TaskList(
                            tasks = tasks,
                            onClickRow = {
                                viewModel.setEditingTask(it)
                                viewModel.isShowDialog = true
                            },
                            onClickDelete = { viewModel.deleteTask(it) },
                            onClickPlayPomo = {task ->
                                navController.navigate("pomodoro/${task.title}")
                            },
                            onClickDone = {
                                viewModel.CheckBoxDone(it)
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(1f)) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(1F), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                val imageResource: Painter = painterResource(R.drawable.ic_empty_tas)
                                Image(painter = imageResource, contentDescription = "My Image")
                                Text(text = "Kamu Belum Memiliki Tugas Apapun Disini", style = Ket_2, color = MaterialTheme.colors.Grey)
                                Text(text = "Tambah Tugasmu di Menu Project", style = Subtitle_2, color = MaterialTheme.colors.FontColor)
                            }
                        
                        }
                        
                    }
                }
            }
            composable(
                "pomodoro/{taskName}",
                arguments = listOf(navArgument("taskName") { defaultValue = "" }),
            ) { backStackEntry ->
                var taskName = backStackEntry.arguments?.getString("taskName") ?: ""
                PomodoroScreen(onBackPressed = {
                    taskName = ""
                    navController.popBackStack()

                },  taskName = taskName,)


            }
        }


    }
}

