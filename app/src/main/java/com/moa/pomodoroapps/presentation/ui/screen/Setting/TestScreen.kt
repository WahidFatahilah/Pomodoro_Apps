package com.moa.pomodoroapps.presentation.ui.screen.Setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun TestScreen(
    navController: NavController
){
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { navController.popBackStack() }) {
            Text("Go back to Setting Screen")
        }
    }
}