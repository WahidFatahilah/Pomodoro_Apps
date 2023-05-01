package com.moa.pomodoroapps.todo.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moa.pomodoroapps.presentation.ui.screen.HOME.MainViewModel
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_2
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EditDialog(
    viewModel: MainViewModel = hiltViewModel(),
) {
    DisposableEffect(Unit ){
        onDispose {
            viewModel.resetProperties()
        }
    }
    var pickedDate by remember {  mutableStateOf(LocalDate.now()) }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDate)
        }
    }


    AlertDialog(
            onDismissRequest = { viewModel.isShowDialog = false },
            title = { Text(text = if (viewModel.isEditing) "Task Update" else "Create New Task") },
            text = {
                Column() {
                    Text(text = "Title")
                    TextField(value = viewModel.title, onValueChange = { viewModel.title = it })
                    Text(text = "Description")
                    TextField(
                        value = viewModel.description,
                        onValueChange = { viewModel.description = it })

                    Text(text = "Deadline", style = Heading_H2, color = MaterialTheme.colors.FontColor, modifier = Modifier.padding(10.dp))

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
                }
            },
            buttons = {
                Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.width(120.dp),
                        onClick = { viewModel.isShowDialog = false }) {
                        Text(text = "cancel")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(modifier = Modifier.width(120.dp), onClick = {
                        viewModel.isShowDialog = false
                        if (viewModel.isEditing) {
                            viewModel.updateTask()
                        } else {
                            viewModel.createTask()
                        }

                    }) {
                        Text(text = "OK")
                    }

                }
            }
        )

}

