package com.moa.pomodoroapps.presentation.ui.screen.Project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moa.pomodoroapps.MainViewModel
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_2
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColorProject
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ProjectScreen(viewModel: MainViewModel = hiltViewModel(),) {
    var pickedDate by remember {  mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {  derivedStateOf {  DateTimeFormatter .ofPattern("MMM dd yyyy").format(pickedDate) } }
    val dateDialogState = rememberMaterialDialogState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(25.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        DisposableEffect(Unit) {
            onDispose {
                viewModel.resetProperties()
            }

        }
        Column(
            modifier = Modifier
                .padding(start = 20.dp, top = 25.dp, end = 20.dp, bottom = 40.dp)
                .background(
                    color = MaterialTheme.colors.backgroundColorProject,
                    shape = RoundedCornerShape(25.dp)
                )
                .clip(
                    RoundedCornerShape(25.dp)
                ),
        ) {
            Text(
                text = "Project Name",
                style = Heading_H2,
                color = MaterialTheme.colors.FontColor,
                modifier = Modifier.padding(10.dp)
            )
            TextField(value = viewModel.project
                , leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable .ic_project) ,
                    contentDescription = "Project",
                    tint = MaterialTheme.colors.FontColor,
                )
            }, onValueChange = { viewModel.project = it }, modifier = Modifier.padding(start = 10.dp))
            Text(text = "Task Name", style = Heading_H2, color = MaterialTheme.colors.FontColor, modifier = Modifier.padding(10.dp))
            TextField(value = viewModel.title, leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable .ic_task) ,
                    contentDescription = "Project",
                    tint = MaterialTheme.colors.FontColor,
                )
            }, onValueChange = { viewModel.title = it }, modifier = Modifier.padding(start = 10.dp))

            Text(text = "Description", style = Heading_H2, color = MaterialTheme.colors.FontColor, modifier = Modifier.padding(10.dp))
            TextField(
                value = viewModel.description, leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable .baseline_description_24) ,
                        contentDescription = "Project",
                        tint = MaterialTheme.colors.FontColor,
                    )
                },
                onValueChange = { viewModel.description = it }, modifier = Modifier.padding(start = 10.dp))
            Text(text = "Deadline", style = Heading_H2, color = MaterialTheme.colors.FontColor, modifier = Modifier.padding(10.dp))
            var pickedDate by remember {
                mutableStateOf(LocalDate.now())
            }
            val formattedDate by remember {
                derivedStateOf {
                    DateTimeFormatter
                        .ofPattern("MMM dd yyyy")
                        .format(pickedDate)
                }
            }

            val dateDialogState = rememberMaterialDialogState()
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Button(onClick = {
                    dateDialogState.show()
                }) {
                    Text(text = "Pick date", style = Subtitle_2, color = MaterialTheme.colors.FontColor)
                }
                Text(text = formattedDate)
                Spacer(modifier = Modifier.height(16.dp))
            }
            MaterialDialog(
                dialogState = dateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                    }
                    negativeButton(text = "Cancel")
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date",
                ) {
                    pickedDate = it
                }
            }

            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = { viewModel.isShowDialog = false }
                ) {
                    Text(text = "cancel")
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        viewModel.isShowDialog = true
                    }
                ) {
                    Text(text = "OK")
                }
            }

            if (viewModel.isShowDialog) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.project = ""
                        viewModel.isShowDialog = false
                        viewModel.title = ""
                        viewModel.description = ""
                    },
                    text = {
                        Text(text = "Are you sure you want to proceed?")
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.deadline =  Date.from(pickedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                            viewModel.isShowDialog = false
                            if (viewModel.isEditing) {
                                viewModel.updateTask()
                            } else {
                                viewModel.createTask()
                            }
                            viewModel.description =" "
                            viewModel.title =" "
                            viewModel.project =" "


                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            viewModel.isShowDialog = false
                            viewModel.title = ""
                            viewModel.description = ""
                        }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}