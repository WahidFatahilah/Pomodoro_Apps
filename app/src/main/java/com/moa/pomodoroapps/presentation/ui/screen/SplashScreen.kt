package com.moa.pomodoroapps.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Ket_1
import com.moa.pomodoroapps.presentation.ui.theme.Pink
import com.moa.pomodoroapps.presentation.ui.theme.Primary

@Composable
fun SplashScreen(){
    Column(modifier = Modifier.padding(start = 30.dp, top = 30.dp, end = 30.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Pomodoro", style = Primary, color = MaterialTheme.colors.Pink)
        Text(text = "Timer.", style = Primary, color = MaterialTheme.colors.Pink)
        Text(text = "Effective Productivity Technique", style = Ket_1, color = MaterialTheme.colors.FontColor)
        val imageResource: Painter = painterResource(R.drawable.img_splash)
        Image(painter = imageResource, contentDescription = "My Image")
    }
}