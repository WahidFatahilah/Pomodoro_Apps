package com.moa.pomodoroapps.presentation.ui.screen.Project

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moa.pomodoroapps.MainViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ProjectScreen(viewModel: MainViewModel = hiltViewModel(),) {
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(pickedDate)
        }
    }
    val dateDialogState = rememberMaterialDialogState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DisposableEffect(Unit) {
            onDispose {
                viewModel.resetProperties()
            }

        }

        Column {
            Text(text = "Project Name")
            TextField(
                value = viewModel.projectName,
                onValueChange = { viewModel.projectName = it }
            )

            Spacer(modifier = Modifier.height(39.dp))

            // Task Form
            Text(text = "Task Name")
            TextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description")
            TextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Deadline")
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { dateDialogState.show() }) {
                    Text(text = "Pick date")
                }
                Text(text = formattedDate)
            }

            MaterialDialog(
                dialogState = dateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {}
                    negativeButton(text = "Cancel")
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date"
                ) {
                    pickedDate = it
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = { viewModel.isShowDialog = false }
                ) {
                    Text(text = "Cancel")
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        viewModel.isShowDialog = true
                    }
                ) {
                    Text(text = "Tambah Project Baru")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isShowDialog) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.isShowDialog = false
                        viewModel.resetProperties()
                    },
                    text = {
                        Text(text = "Are you sure you want to proceed ?")
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.isShowDialog = false
                            viewModel.createProjectWithTask()
                            viewModel.resetProperties()
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            viewModel.isShowDialog = false
                            viewModel.resetProperties()
                        }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}






/*


@Composable
fun ProjectScreen(viewModel: MainViewModel = hiltViewModel(),) {
    var pickedDate by remember {  mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {  derivedStateOf {  DateTimeFormatter .ofPattern("MMM dd yyyy").format(pickedDate) } }
    val dateDialogState = rememberMaterialDialogState()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        DisposableEffect(Unit) {
            onDispose {
                viewModel.resetProperties()
            }

        }
        Column() {

            Text(text = "Project Name")
            TextField(value = viewModel.project, onValueChange = { viewModel.project = it })
            Text(text = "Project Name")
            TextField(value = viewModel.title, onValueChange = { viewModel.title = it })

            Text(text = "Description")
            TextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it })
            Text(text = "Deadline")
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
            ) {
                Button(onClick = {
                    dateDialogState.show()
                }) {
                    Text(text = "Pick date")
                }
                Text(text = formattedDate)
                Spacer(modifier = Modifier.height(16.dp))
            }
            MaterialDialog(
                dialogState = dateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        */
/* Toast.makeText(
                             applicationContext,
                             "Clicked ok",
                             Toast.LENGTH_LONG
                         ).show()*//*

                    }
                    negativeButton(text = "Cancel")
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date",
                    */
/*allowedDateValidator = {
                    }*//*

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
                        */
/*if (viewModel.isEditing) {
                            viewModel.updateProject()
                        } else {
                            viewModel.createProject()
                        }*//*

                    }
                ) {
                    Text(text = "OK")
                }
            }

            if (viewModel.isShowDialog) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.isShowDialog = false
                        viewModel.title = ""
                        viewModel.description = ""
                    },
                    text = {
                        Text(text = "Are you sure you want to proceed?")
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.isShowDialog = false
                            if (viewModel.isEditing) {
                                viewModel.updateProject()
                            } else {
                                viewModel.createProject()
                            }
                            viewModel.description =" "
                            viewModel.title =" "


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
}*/
