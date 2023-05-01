package com.moa.pomodoroapps.presentation.ui.screen.Pomodoro

import PlayLocalAudio
import android.print.PrintAttributes.Margins
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
    durationPomodoro: Long,
    isPaused: MutableState<Boolean>,
    isRunning: MutableState<Boolean>,
    sessionCount: Int,
    onSessionComplete: () -> Unit,
    remainingTimePomodoro: Long,
    project:String,
    task:String,

    ){

    var isSoundOn  by remember { mutableStateOf(true)}

    var text = "Pomodoro Sesi $sessionCount/3"

    var isPlayAudio by remember { mutableStateOf(true) }
    if (!isPaused.value){
        PlayLocalAudio()
    }else {  }

    Column(modifier = Modifier.fillMaxSize()) {
        topbar(title = project, isSoundOn = isSoundOn)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .widthIn(min = 200.dp, max = 500.dp)
                    .heightIn(min = 200.dp, max = 500.dp)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            )
            {
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
            Column(
                modifier = Modifier.fillMaxSize(0.5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = task, style = Heading_H1, color = MaterialTheme.colors.Grey)
                Text(text = text, style = Ket_2, color = MaterialTheme.colors.Grey)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp),
                contentAlignment = Alignment.Center
            ){

                Row(
                    //modifier = Modifier.align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box() {
                        if (isPaused.value){
                            OutlinedButton(modifier= Modifier.size(100.dp),
                                onClick = {   isPaused.value = false
                                    isRunning.value = true},
                                enabled = isPaused.value,
                                shape = CircleShape,
                               // border= BorderStroke(1.dp, Color.Red),
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
                                { isPaused.value = true  }, Modifier.size(100.dp), !isPaused.value,
                                shape = CircleShape,
                            //    border= BorderStroke(1.dp, Color.Red),
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
                    Spacer(modifier = Modifier.width(30.dp))
                    Box() {
                        OutlinedButton(onClick = {
                            isRunning.value = false
                            onSessionComplete()},
                            enabled = !isPaused.value,
                            modifier= Modifier.size(100.dp),
                            shape = CircleShape,
                        //    border= BorderStroke(1.dp, Color.Red),
                            contentPadding = PaddingValues(0.dp),
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
    isSoundOn : Boolean,
) {
    TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { isSoundOn },modifier = Modifier.align(Alignment.CenterVertically)) {
                Image(
                    painter = painterResource(id = com.moa.pomodoroapps.R.drawable.ic_back),
                    contentDescription = "Navigation Icon"
                )
            }

            Text(
                text = title,
                style = Heading_H1,
                color = MaterialTheme.colors.FontColor,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)

            )
            Spacer(
                Modifier
                    .width(15.dp)
                    .background(color = MaterialTheme.colors.Grey))
            if (isSoundOn){
                IconButton(onClick = { isSoundOn },modifier = Modifier.align(Alignment.CenterVertically)) {
                    Image(
                        painter = painterResource(id = com.moa.pomodoroapps.R.drawable.ic_sound_on),
                        contentDescription = "Navigation Icon",
                    )
                }
            }else{
                IconButton(onClick = { /* TODO */ },modifier = Modifier.align(Alignment.CenterVertically)) {
                    Image(
                        painter = painterResource(id = com.moa.pomodoroapps.R.drawable.ic_sound_off),
                        contentDescription = "Navigation Icon"
                    )
                }
            }

        }
    }
    Divider(
        color = MaterialTheme.colors.Grey,
        thickness = 2.dp,
        modifier = Modifier.padding(start = 25.dp, top = 2.dp, end = 25.dp, bottom = 5.dp)
    )

}
