package com.moa.pomodoroapps.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.moa.pomodoroapps.R


@Composable
fun SplashScreen() {
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Column {
            Text(text = "Pomodoro")
            Text(text = "Timer")
            Text(text = "Effective Productivity Technique")
            Image(contentDescription = "", painter = painterResource(id = R.drawable.splashimage))
        }
    }





}