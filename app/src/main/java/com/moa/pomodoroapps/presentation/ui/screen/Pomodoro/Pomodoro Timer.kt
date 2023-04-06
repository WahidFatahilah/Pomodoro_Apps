package com.moa.pomodoroapps.presentation.ui.screen.HOME

import PlayLocalAudio
import android.graphics.Paint.Style
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.*


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
    durationLongBreakkk: Long,
    isPomodoro: Boolean,
    isShortBreak: Boolean,
    isLongBreakkk: Boolean,
    remainingTimePomodoro: Long,
    remainingTimeShortBreak: Long,
    remainingTimeLongBreakkk: Long,

){
    var text = ""
    if (isPomodoro == true){
        text = "Pomodoro Sesi $sessionCount"
    }else if (isShortBreak == true){
        text = "Istirahat Sesi $sessionCount"
    }else if (isLongBreakkk == true){
        text = "Istirahat Sesi $sessionCount"
    }
    var isPlayAudio by remember {mutableStateOf(true)}
    if (!isPaused.value){
        PlayLocalAudio()
    }else { }



    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        topbar(title = "ss")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        )
        {
            if(isLongBreakkk) {
                CircularProgressBar(
                    progress = (durationLongBreakkk - remainingTimeLongBreakkk/ 1000f) / durationLongBreakkk,
                    modifier = Modifier.size(400.dp),
                    color = MaterialTheme.colors.Yellow,
                    strokeWidth = 35.dp,
                )
                Column() {
                    Text(
                        text = formatTime(remainingTimeLongBreakkk),
                        fontSize = 50.sp,
                        color = MaterialTheme.colors.Yellow,
                        fontWeight = FontWeight.Bold
                    )
                    SessionIcons(sessionCount = sessionCount)
                }


            }

            if(isPomodoro) {
                CircularProgressBar(
                    progress = (durationPomodoro - remainingTimePomodoro / 1000f) / durationPomodoro,
                    modifier = Modifier.size(400.dp),
                    color = MaterialTheme.colors.Pink,
                    strokeWidth = 35.dp,
                )
                Column() {
                    Text(
                        text = formatTime(remainingTimePomodoro),
                        fontSize = 50.sp,
                        color = MaterialTheme.colors.FontColor,
                        fontWeight = FontWeight.Bold
                    )
                    SessionIcons(sessionCount = sessionCount)
                }


            }
            if(isShortBreak) {
                CircularProgressBar(
                    progress = (durationShortBreak - remainingTimeShortBreak / 1000f) / durationShortBreak,
                    modifier = Modifier.size(400.dp),
                    color = MaterialTheme.colors.Yellow,
                    strokeWidth = 35.dp,
                )
                Column() {
                    Text(
                        text = formatTime(remainingTimeShortBreak),
                        fontSize = 50.sp,
                        color = MaterialTheme.colors.FontColor,
                        fontWeight = FontWeight.Bold
                    )
                    SessionIcons(sessionCount = sessionCount)
                }



            }



        }
        Column(
            modifier = Modifier.fillMaxSize(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Session $sessionCount/3", style = Heading_H2, color = MaterialTheme.colors.Grey)
            Text(text = text, style = Ket_2, color = MaterialTheme.colors.Grey)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){

            Row(
                //modifier = Modifier.align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
               Box() {
                   if (isPaused.value){
                       OutlinedButton(modifier= Modifier.size(50.dp),
                           onClick = {   isPaused.value = false
                               isRunning.value = true},
                           enabled = isPaused.value,
                           shape = CircleShape,
                           border= BorderStroke(1.dp, Color.Red),
                           contentPadding = PaddingValues(0.dp),  //avoid the little icon
                           colors = ButtonDefaults.outlinedButtonColors(contentColor =  Color.Blue)
                       ) {
                           Image(
                               painter = painterResource(R.drawable.baseline_play_circle_filled_24),
                               contentDescription = "avatar",
                               // crop the image if it's not a square
                               modifier = Modifier
                                   .size(64.dp)
                                   .clip(CircleShape)                       // clip to the circle shape
                                   .border(2.dp, Color.Gray, CircleShape)
                               , colorFilter = ColorFilter.tint(MaterialTheme.colors.Pink)// add a border (optional)
                           )

                       }
                   }else {
                       OutlinedButton(
                           onClick = { isPaused.value = true  },
                           enabled = !isPaused.value,
                           modifier= Modifier.size(50.dp),  //avoid the oval shape
                           shape = CircleShape,
                           border= BorderStroke(1.dp, Color.Red),
                           contentPadding = PaddingValues(0.dp),  //avoid the little icon
                           colors = ButtonDefaults.outlinedButtonColors(contentColor =  MaterialTheme.colors.Pink)
                       ) {
                           Image(
                               painter = painterResource(R.drawable.baseline_pause_circle_filled_24),
                               contentDescription = "avatar",
                               modifier = Modifier
                                   .size(64.dp)
                                   .clip(CircleShape)
                                   .border(2.dp, Color.Gray, CircleShape)
                               , colorFilter = ColorFilter.tint(MaterialTheme.colors.Pink)
                           )

                       }
                   }
                   }

                OutlinedButton(onClick = {
                    isRunning.value = false
                    onSessionComplete()},
                    enabled = !isPaused.value,
                    modifier= Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    border= BorderStroke(1.dp, Color.Red),
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(contentColor =  MaterialTheme.colors.Pink)
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_stop_24),
                        contentDescription = "avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                        , colorFilter = ColorFilter.tint(MaterialTheme.colors.Pink)
                    )

                }

            }
        }

    }
}



private fun formatTime(time: Long): String {
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

@Composable
fun topbar(
    title: String,
) {
    Spacer(Modifier.width(2.dp))
    TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                //style = h1,
                //color = MaterialTheme.colors.font,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)

            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { /* TODO */ },modifier = Modifier.align(Alignment.CenterVertically)) {
                /*Image(
                    painter = painterResource(id = com.moa.pomodoroapps.R.drawable.icon_delete),
                    contentDescription = "Navigation Icon"
                )*/
            }
        }
    }
    Divider(
        color = MaterialTheme.colors.onSurface,
        thickness = 0.5.dp,
        modifier = Modifier.padding(start = 25.dp, top = 2.dp, end = 25.dp, bottom = 5.dp)
    )

}

@Composable
fun SessionIcons(sessionCount: Int) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (sessionIndex in 1..3) {
            val isCompleted = sessionIndex <= sessionCount
            iconForSession(sessionIndex, isCompleted)
        }
    }
}

@Composable
private fun iconForSession(sessionIndex: Int, isCompleted: Boolean) {
    var sessionCount = sessionIndex
    val icon: ImageVector = when {
        isCompleted -> Icons.Filled.CheckCircle
        sessionIndex == sessionCount + 1 -> Icons.Default.Lock
        else -> Icons.Filled.Lock
    }

    Icon(
        imageVector = icon,
        contentDescription = "Session $sessionIndex",
        modifier = Modifier.padding(horizontal = 8.dp),
        tint = if (isCompleted) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium)
    )
}