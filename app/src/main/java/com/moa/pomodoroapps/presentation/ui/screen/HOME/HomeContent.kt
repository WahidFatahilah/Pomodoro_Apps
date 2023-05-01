package com.moa.pomodoroapps.presentation.ui.screen.HOME

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.format.DateUtils.isToday
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.R

import com.moa.pomodoroapps.presentation.navigation.topnav.CustomTopAppBar
import com.moa.pomodoroapps.presentation.ui.feature.appbarcustom.system.CustomCircularBar
import com.moa.pomodoroapps.presentation.ui.screen.Pomodoro.PomodoroScreen
import com.moa.pomodoroapps.presentation.ui.theme.*
import com.moa.pomodoroapps.todo.components.EditDialog
import com.moa.pomodoroapps.todo.components.TaskList
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
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
                  //  CardHome(totalTask = totalTask, totalTaskDone = totalTaskDone)

                    Card(
                        shape = RoundedCornerShape(19.dp),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .heightIn(min = 30.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        backgroundColor = MaterialTheme.colors.Pink,
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {

                                Card(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    backgroundColor = MaterialTheme.colors.PinkSoft
                                ) {
                                    Column(modifier = Modifier
                                        .heightIn(30.dp, 100.dp)
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp,
                                            top = 6.dp,
                                            end = 20.dp,
                                            bottom = 6.dp
                                        ), verticalArrangement = Arrangement.Top) {
                                        Text(
                                            text = stringResource(R.string.total_task),
                                            style = Heading_H2,
                                            color = asset_White,
                                            modifier = Modifier
                                            //     .padding(top = 7.dp, start = 12.dp, end = 31.dp, bottom = 31.dp),

                                        )
                                        Text(
                                            text = totalTask.toString() + stringResource(id = R.string.task),
                                            //"0 Project",
                                            style = Ket_2,
                                            color = asset_White,
                                            modifier = Modifier
                                            // .padding(top = 37.dp, start = 12.dp, end = 80.dp, bottom = 7.dp),



                                        )
                                    }


                                }

                                Card(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    backgroundColor = MaterialTheme.colors.PinkSoft
                                ) {
                                    Column(modifier = Modifier
                                        .heightIn(30.dp, 100.dp)
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp,
                                            top = 6.dp,
                                            end = 20.dp,
                                            bottom = 6.dp
                                        ), verticalArrangement = Arrangement.Top) {
                                        Text(
                                            text = stringResource(R.string.task_done),
                                            style = Heading_H2,
                                            color = asset_White,
                                            modifier = Modifier
                                            //     .padding(top = 7.dp, start = 12.dp, end = 31.dp, bottom = 31.dp),

                                        )
                                        Text(
                                            text = totalTaskDone.toString() + stringResource(id = R.string.task),
                                            //"0 Project",
                                            style = Ket_2,
                                            color = asset_White,
                                            modifier = Modifier
                                            // .padding(top = 37.dp, start = 12.dp, end = 80.dp, bottom = 7.dp),



                                        )
                                    }


                                }

                            }

                            Box(
                                modifier = Modifier .fillMaxWidth()
                            ){
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(150.dp, 250.dp),
                                ){

                                   var circleCenter = Offset.Zero

                                   var positionValue = totalPercentage

                                    val width = size.width
                                    val height = size.height
                                    val circleThickness = width/10f
                                    circleCenter = Offset(x = width/2f, y = height/2f)
                                    var maxValue : Int =  100

                                    drawCircle(
                                        color = PinkSoft,
                                        radius = 100f - 30f ,
                                        center = circleCenter
                                    )

                                    drawCircle(
                                        style = Stroke(
                                            width = circleThickness
                                        ),
                                        color = asset_White,
                                        radius = 100f,
                                        center = circleCenter,

                                        )


                                    drawArc(
                                        color = Color.Yellow,
                                        startAngle = -90f,
                                        sweepAngle = (360f/maxValue) * positionValue.toFloat(),
                                        style = Stroke(
                                            width = circleThickness,
                                            cap = StrokeCap.Round

                                        ),
                                        useCenter = false,
                                        size = Size(
                                            width = 100f * 2f,
                                            height= 100f * 2f
                                        ),
                                        topLeft = Offset(
                                            (width - 100f * 2f)/2f,
                                            (height - 100f * 2f)/2f
                                        )
                                    )
                                    drawContext.canvas.nativeCanvas.apply {
                                        drawIntoCanvas {
                                            drawText(
                                                "$positionValue%",
                                                circleCenter.x,
                                                circleCenter.y + 45.dp.toPx()/8f,
                                                Paint().apply {
                                                    textSize = 16.sp.toPx()
                                                    textAlign = Paint.Align.CENTER
                                                    color = asset_White.toArgb()
                                                    isFakeBoldText = true
                                                }
                                            )
                                        }
                                    }
                                }


                            }



                        }

                    }



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

@Composable
private fun EmptyTaskAndProject() {
    Box(modifier = Modifier.fillMaxSize(1f)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1F),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageResource: Painter = painterResource(R.drawable.ic_empty_tas)
            Image(painter = imageResource, contentDescription = "My Image")
            Text(
                text = stringResource(R.string.dont_have_task),
                style = Ket_2,
                color = MaterialTheme.colors.Grey
            )
            Text(
                text = stringResource(R.string.please_add_task),
                style = Subtitle_2,
                color = MaterialTheme.colors.FontColor
            )
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
