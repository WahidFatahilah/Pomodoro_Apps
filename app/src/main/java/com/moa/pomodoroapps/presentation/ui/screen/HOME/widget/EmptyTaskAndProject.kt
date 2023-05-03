package com.moa.pomodoroapps.presentation.ui.screen.HOME.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Grey
import com.moa.pomodoroapps.presentation.ui.theme.Ket_2
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_2

@Composable
fun EmptyTaskAndProject() {
    Box(modifier = Modifier.fillMaxSize(1f)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1F),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageResource: Painter = painterResource(R.drawable.ic_empty_tas)
            Image(painter = imageResource, contentDescription = "My Image")
            Text(
                text = stringResource(R.string.dont_have_task),
                style = Ket_2,
                color = MaterialTheme.colors.Grey
            )
            Text(
                text = stringResource(R.string.please_add_task),
                style = Subtitle_2,
                color = MaterialTheme.colors.FontColor
            )
        }
    }
}