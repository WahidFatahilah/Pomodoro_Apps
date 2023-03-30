package com.moa.pomodoroapps.presentation.ui.screen.Project

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moa.pomodoroapps.MainViewModel


@Composable
fun ProjectScreen(viewModel: MainViewModel = hiltViewModel(),) {


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        DisposableEffect(Unit) {
            onDispose {
                viewModel.resetProperties()
            }

        }
        Column() {
            Text(text = "Title")
            TextField(value = viewModel.title, onValueChange = { viewModel.title = it })
            Text(text = "Description")
            TextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it })
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
                        /*if (viewModel.isEditing) {
                            viewModel.updateTask()
                        } else {
                            viewModel.createTask()
                        }*/
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
                                viewModel.updateTask()
                            } else {
                                viewModel.createTask()
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
}