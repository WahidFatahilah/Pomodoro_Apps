package com.moa.pomodoroapps.presentation.ui.screen.HOME

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moa.pomodoroapps.MainViewModel
import com.moa.pomodoroapps.presentation.ui.screen.PomodoroScreen
import com.moa.pomodoroapps.presentation.ui.theme.*
import com.moa.pomodoroapps.todo.components.EditDialog
import com.moa.pomodoroapps.todo.components.TaskList
import kotlinx.coroutines.Job


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeContent(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {
    val navController = rememberNavController()


    if (viewModel.isShowDialog) {
        EditDialog()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val tasks by viewModel.tasks.collectAsState(initial = emptyList())
        NavHost(navController, startDestination = "tasks") {
            composable("tasks") {
                TaskList(
                    tasks = tasks,
                    onClickRow = {
                        viewModel.setEditingTask(it)
                        viewModel.isShowDialog = true
                    },
                    onClickDelete = { viewModel.deleteTask(it) },
                    onClickPlayPomo = { task ->
                        navController.navigate("pomodoro/${tasks}")
                    },
                    onClickDone= {
                        viewModel.CheckBoxDone(it)
                    }
                )
            }
            composable(
                "pomodoro/{taskName}",
                arguments = listOf(navArgument("taskName") { defaultValue = "" })
            ) { backStackEntry ->
                val taskName = backStackEntry.arguments?.getString("taskName") ?: ""
                //navController.navigate(PomodoroScreen())
                PomodoroScreen()
            }
        }
    }
    /*Scaffold(
        *//*floatingActionButton = {
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
    },*//*
        modifier = Modifier.padding(bottom = 64.dp)
    ) { //it ->
        innerPadding ->
        val tasks by viewModel.tasks.collectAsState(initial = emptyList())
        *//*TaskList(
            tasks = tasks,
            onClickRow = {
                viewModel.setEditingTask(it)
                viewModel.isShowDialog = true
            },
            onClickDelete = { viewModel.deleteTask(it) },
            onClickPlayPomo = {
               *//**//* goto pomodoro timer with  send data task name  *//**//*
            }
        )*//*
        NavHost(navController, startDestination = "tasks") {
            composable("tasks") {
                TaskList(
                    tasks = tasks,
                    onClickRow = {
                        viewModel.setEditingTask(it)
                        viewModel.isShowDialog = true
                    },
                    onClickDelete = { viewModel.deleteTask(it) },
                    onClickPlayPomo = { task ->
                        navController.navigate("pomodoro/${tasks}")
                    }
                )
            }
            composable(
                "pomodoro/{taskName}",
                arguments = listOf(navArgument("taskName") { defaultValue = "" })
            ) { backStackEntry ->
                val taskName = backStackEntry.arguments?.getString("taskName") ?: ""
                //navController.navigate(PomodoroScreen())
                PomodoroScreen()
            }

        }

    }
*/
}


@Composable
fun PomodoroTimer(
    taskName: String,
    navController: NavController
) {
    var isRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(25 * 60) }
    var timeStarted by remember { mutableStateOf(25 * 60) }
    var job: Job? = null
    val coroutineScope = rememberCoroutineScope()
    val task = taskName.substringAfter("(").substringBefore(")")
    val title = task.substringAfter("title=").substringBefore(",")

    Text(
        text = title,
    )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(250.dp),
                shape = CircleShape,
                color = Color.White,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                       /* text = formatTime(timeLeft),*/
                        text = "sss",
                        color = Color.Black,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        /*TimerButton(
                            //text = if (isRunning) stringResource(R.string.pause) else stringResource(R.string.start),
                            enabled = timeLeft > 0,
                            onClick = {
                                if (isRunning) {
                                    job?.cancel()
                                    isRunning = false
                                    timeStarted = timeLeft
                                } else {
                                    isRunning = true
                                    job = coroutineScope.launch {
                                        while (timeLeft > 0) {
                                            timeLeft--
                                            delay(1000L)
                                        }
                                        isRunning = false
                                    }
                                }
                            }
                        )*/
                        TimerButton(
                            /*text = stringResource(R.string.reset),*/
                            text = "sss",
                            enabled = !isRunning && timeLeft < timeStarted,
                            onClick = {
                                isRunning = false
                                timeLeft = timeStarted
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            ProgressIndicator(
                progress = if (timeLeft > 0) 1 - timeLeft.toFloat() / timeStarted.toFloat() else 1f
            )
        }
    }

@Composable
fun TimerButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (enabled) MaterialTheme.colors.primary else Color.LightGray
        )
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun ProgressIndicator(progress: Float) {
    Box(
        modifier = Modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        /*Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2 - 16.dp.toPx()
            drawCircle(
                color = Color.LightGray,
                radius = radius,
                style = Stroke(width = 32.dp.toPx())
            )
            drawArc(
                color = MaterialTheme.colors.primary,
                startAngle = -PI.toFloat() / 2,
                sweepAngle = progress * 2 * PI.toFloat(),
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 32.dp.toPx())
            )
        }
        Text(
            text = formatPercent(progress),
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.clip(CircleShape)
        )
    }*/
}

fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}

fun formatPercent(progress: Float): String {
    return "%.0f%%".format(progress * 100)
}
}