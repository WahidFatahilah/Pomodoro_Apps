package com.moa.pomodoroapps.presentation.ui.screen.Pomodoro

import PlayLocalAudio
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.*

@Composable
fun TimerLongBreak(
    isPaused: MutableState<Boolean>,
    isRunning: MutableState<Boolean>,
    sessionCount: Int,
    onSessionComplete: () -> Unit,
    durationLongBreakkk: Long,
    remainingTimeLongBreakkk: Long,
    project:String,
    task:String,

    ){
    var isSoundOn  by remember { mutableStateOf(true)}
    var text = "Istirahat Panjang $sessionCount"

    var isPlayAudio by remember { mutableStateOf(true) }
    if (!isPaused.value){
        PlayLocalAudio()
    }else { }



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
                    progress = (durationLongBreakkk - remainingTimeLongBreakkk/ 1000f) / durationLongBreakkk,
                    modifier = Modifier.size(400.dp),
                    color = MaterialTheme.colors.Blue,
                    strokeWidth = 35.dp,
                )
                Column() {
                    Text(
                        text = formatTime(remainingTimeLongBreakkk),
                        fontSize = 50.sp,
                        color = MaterialTheme.colors.BlueSoft,
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

                Text(text = "Session $sessionCount/3", style = Heading_H2, color = MaterialTheme.colors.Grey)
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
