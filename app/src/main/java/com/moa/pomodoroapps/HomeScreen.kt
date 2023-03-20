package com.moa.pomodoroapps

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.ui.theme.FontColor
import com.moa.pomodoroapps.ui.theme.Heading_H2


@Composable
fun HomeScreen() {
    Column(){
        Text(
            text = "Tugas",
            style = Heading_H2,
            color = MaterialTheme.colors.FontColor,
            modifier = Modifier
                .padding(16.dp, 14.dp, 0.dp, 0.dp)
                .fillMaxWidth()
        )

    }
}
