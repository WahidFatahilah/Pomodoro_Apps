package com.moa.pomodoroapps.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.todo.data.MainContent
import com.moa.pomodoroapps.ui.theme.FontColor
import com.moa.pomodoroapps.ui.theme.Heading_H2

@ExperimentalPagerApi
@Composable
fun HomeScreen() {

    Row(){
        Text(
            text = "Tugas",
            style = Heading_H2,
            color = MaterialTheme.colors.FontColor,
            modifier = Modifier
                .padding(16.dp, 14.dp, 0.dp, 0.dp)
                .fillMaxWidth()
        )

    }

    Row(
        modifier = Modifier.padding(top = 64.dp)
    ) {
        MainContent()
    }
}





