package com.moa.pomodoroapps.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.ui.theme.font
import com.moa.pomodoroapps.ui.theme.keterangan1
import com.moa.pomodoroapps.ui.theme.pink
import com.moa.pomodoroapps.ui.theme.primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun SplashScreen() {

    val coroutineScope = rememberCoroutineScope()
    var showSurface by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {

        coroutineScope.launch {
            delay(3000)
            showSurface = true
        }
    }

    if (showSurface) {
        Surface(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colors.pink)) {

        }

    }else {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
            .background(color = MaterialTheme.colors.background)) {
            Column {
                Text(text = "Pomodoro", style = primary.copy(color = MaterialTheme.colors.pink))
                Text(text = "Timer", style = primary.copy(color = MaterialTheme.colors.pink))
                Text(text = "Effective Productivity Technique" ,style = keterangan1.copy(color = MaterialTheme.colors.font))
                Image(contentDescription = "", painter = painterResource(id = R.drawable.splashimage))
            }

        }

    }



}