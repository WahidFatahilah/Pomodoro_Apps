package com.moa.pomodoroapps.presentation.ui.screen.HOME

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TimerPomodoro(
    nameList: List<String>,
    selectedName: String,
    durationPomodoro: Long,
    //remainingTime: Long,
    isPaused: MutableState<Boolean>,
    isRunning: MutableState<Boolean>,
    sessionCount: Int,
    onSessionComplete: () -> Unit,
    durationShortBreak: Long,
    durationLongBreak: Long,
    isPomodoro: Boolean,
    isShortBreak: Boolean,
    isLongBreak: Boolean,
    remainingTimePomodoro: Long,
    remainingTimeShortBreak: Long,
    remainingTimeLongBreak: Long,
    sessionCountPomodoro: Int,
    sessionCountShortBreak: Int,
){
    //PlayLocalAudio()
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        )
        {
            if(isPomodoro) {
                CircularProgressBar(
                    progress = (durationPomodoro - remainingTimePomodoro / 1000f) / durationPomodoro,
                    modifier = Modifier.size(400.dp),
                    strokeWidth = 35.dp,
                )
                Text(
                    text = formatTime(remainingTimePomodoro),
                    fontSize = 50.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            if(isShortBreak) {
                CircularProgressBar(
                    progress = (durationShortBreak - remainingTimeShortBreak / 1000f) / durationShortBreak,
                    modifier = Modifier.size(400.dp),
                    color = MaterialTheme.colors.secondary,
                    strokeWidth = 35.dp,
                )
                Text(
                    text = formatTime(remainingTimeShortBreak),
                    fontSize = 50.sp,
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )

            }

            if(isLongBreak) {
                CircularProgressBar(
                    progress = (durationLongBreak - remainingTimeLongBreak/ 1000f) / durationLongBreak,
                    modifier = Modifier.size(400.dp),
                    color = MaterialTheme.colors.secondary,
                    strokeWidth = 35.dp,
                )
                Text(
                    text = formatTime(remainingTimeLongBreak),
                    fontSize = 50.sp,
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )

            }

        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Row(
                //modifier = Modifier.align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { isPaused.value = true },
                    enabled = !isPaused.value
                ) {
                    Text(text = "PAUSE")
                }
                Button(
                    onClick = {
                        isPaused.value = false
                        isRunning.value = true
                    },
                    enabled = isPaused.value
                ) {
                    Text(text = "RESUME")
                }
                Button(
                    onClick = {
                        isRunning.value = false
                        onSessionComplete()
                    },
                    enabled = !isPaused.value
                ) {
                    Text(text = "STOP")
                }
                Text(text = "Session $sessionCount/3", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

    }
}


/*
@Composable
fun PlayLocalAudio() {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.audiopomodoro).apply {
            setOnCompletionListener {
                // Restart the audio when it's finished
                this.start()
            }
        }
    }

    // Start playing the audio when the composable is first displayed
    DisposableEffect(Unit) {
        mediaPlayer.start()

        // Stop the audio when the composable is removed from the screen
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}*/

private fun formatTime(time: Long): String {
    val milliseconds = time % 1000 / 10
    val seconds = time / 1000 % 60
    val minutes = time / 1000 / 60
/*    return "%02d:%02d:%02d".format(minutes, seconds, milliseconds)*/
    return "%02d:%02d".format( minutes, seconds)
}

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    strokeWidth: Dp = 15.dp,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = color.copy(alpha = 0.1f)
) {


    val stroke = with(LocalDensity.current) { strokeWidth.toPx() }
    Canvas(modifier = modifier) {
        val radius = (size.minDimension - stroke) / 2
        val startAngle = 270f
        val sweepAngle = 360 - (progress * 360f)
        drawArc(
            color = backgroundColor,
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(stroke, cap = StrokeCap.Round)
        )
        if (progress == 0f) {
            drawCircle(
                color = backgroundColor,
                radius = radius - stroke / 2,
                center = Offset(size.width / 2, size.height / 2),
                style = Stroke(stroke, cap = StrokeCap.Round)
            )
            drawCircle(
                color = color,
                radius = radius - stroke / 2,
                center = Offset(size.width / 2, size.height / 2),
                style = Stroke(stroke)
            )
        }
    }
}

