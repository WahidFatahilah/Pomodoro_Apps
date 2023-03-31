package com.moa.pomodoroapps.presentation.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun PomodoroScreen() {
    var durationPomodoro by remember { mutableStateOf(15L) }
    var durationShortBreak by remember { mutableStateOf(5L) }
    var durationLongBreak by remember { mutableStateOf(10L) }
    var namaKegiatan by remember { mutableStateOf("") }
    var remainingTimePomodoro by remember { mutableStateOf(10L) }
    var remainingTimeShortBreak by remember { mutableStateOf(10L) }
    var remainingTimeLongBreak by remember { mutableStateOf(10L) }
    var isRunning by remember { mutableStateOf(mutableStateOf(false)) }
    var isPaused by remember { mutableStateOf(mutableStateOf(false)) }
    var showTimerDialog by remember { mutableStateOf(false) }
    val nameList = listOf("Mengerjakan Pr", "Membaca Buku", "Mendengar Podcast",)
    var selectedName by remember { mutableStateOf(nameList[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var sessionCount by remember { mutableStateOf(1) }
    var sessionCountPomodoro by remember { mutableStateOf(1) }
    var sessionCountShortBreak by remember { mutableStateOf(1) }
    var isPomodoro by remember { mutableStateOf(true) }
    var isShortBreak by remember { mutableStateOf(false) }
    var isLongBreak by remember { mutableStateOf(false) }

    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isRunning.value) {
            if(isPomodoro == true){
                TimerPomodoro(
                    nameList = nameList,
                    selectedName = selectedName,
                    durationPomodoro = durationPomodoro,
                    durationShortBreak = durationShortBreak,
                    durationLongBreak = durationLongBreak,
                    remainingTimePomodoro = remainingTimePomodoro,
                    remainingTimeShortBreak = remainingTimeShortBreak,
                    remainingTimeLongBreak = remainingTimeShortBreak,
                    isPaused = isPaused,
                    isRunning = isRunning,
                    isPomodoro = isPomodoro,
                    isShortBreak = isShortBreak,
                    isLongBreak = isLongBreak,
                    sessionCount = sessionCount,
                    sessionCountPomodoro = sessionCountPomodoro,
                    sessionCountShortBreak = sessionCountShortBreak,
                    onSessionComplete = {
                      /*  isRunning.value = false
                        isPomodoro = false
                        isShortBreak = true*/
                        //showDialog = true
                        //sessionCount++
                        /*if (sessionCount <= 3) {
                            showDialog = true
                            remainingTimePomodoro = 300L
                            durationPomodoro = remainingTimePomodoro
                        } else {
                            sessionCount = 1
                            remainingTimePomodoro = 900L
                            durationPomodoro = remainingTimePomodoro
                        }*/
                    }
                )
            }
            if(isShortBreak == true){
                TimerPomodoro(
                    nameList = nameList,
                    selectedName = selectedName,
                    durationPomodoro = durationPomodoro,
                    durationShortBreak = durationShortBreak,
                    durationLongBreak = durationLongBreak,
                    remainingTimePomodoro = remainingTimePomodoro,
                    remainingTimeShortBreak = remainingTimeShortBreak,
                    remainingTimeLongBreak = remainingTimeShortBreak,
                    isPaused = isPaused,
                    isRunning = isRunning,
                    isPomodoro = isPomodoro,
                    isShortBreak = isShortBreak,
                    isLongBreak = isLongBreak,
                    sessionCount = sessionCount,
                    sessionCountPomodoro = sessionCountPomodoro,
                    sessionCountShortBreak = sessionCountShortBreak,
                    onSessionComplete = {
                        /*isRunning.value = false
                        isPomodoro = true
                        isShortBreak = false*/
                        sessionCount++
                        //showDialog = true
                    }
                )
            }
            if(isLongBreak == true) {
                TimerPomodoro(
                    nameList = nameList,
                    selectedName = selectedName,
                    durationPomodoro = durationPomodoro,
                    durationShortBreak = durationShortBreak,
                    durationLongBreak = durationLongBreak,
                    remainingTimePomodoro = remainingTimePomodoro,
                    remainingTimeShortBreak = remainingTimeShortBreak,
                    remainingTimeLongBreak = remainingTimeShortBreak,
                    isPaused = isPaused,
                    isRunning = isRunning,
                    isPomodoro = isPomodoro,
                    isShortBreak = isShortBreak,
                    isLongBreak = isLongBreak,
                    sessionCount = sessionCount,
                    sessionCountPomodoro = sessionCountPomodoro,
                    sessionCountShortBreak = sessionCountShortBreak,
                    onSessionComplete = {
                      /*  isRunning.value = false
                        isLongBreak = false
                        isPomodoro = true*/
                        //showDialog = true
                        //sessionCount == 1
                        //sessionCount++
                    })
            }

        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = durationPomodoro.toString(),
                    onValueChange = { input ->
                        durationPomodoro = if (input.isEmpty()) 0 else input.toLong()
                        remainingTimePomodoro = durationPomodoro * 1000
                    },
                    label = { Text("Duration Pomodoro in seconds") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = durationShortBreak.toString(),
                    onValueChange = { input ->
                        durationShortBreak = if (input.isEmpty()) 0 else input.toLong()
                        remainingTimeShortBreak = durationShortBreak * 1000
                    },
                    label = { Text("Duration Short Break in seconds") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = durationLongBreak.toString(),
                    onValueChange = { input ->
                        durationLongBreak = if (input.isEmpty()) 0 else input.toLong()
                        remainingTimeLongBreak = durationLongBreak * 1000
                    },
                    label = { Text("Duration Long Break in seconds") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isRunning.value = true
                        remainingTimePomodoro = durationPomodoro * 1000
                    }
                ) {
                    Text(text = "START")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (isPomodoro) {
                if (isRunning.value && remainingTimePomodoro > 0 && !isPaused.value) {
                    remainingTimePomodoro -= 1000
                } else if (remainingTimePomodoro == 0L) {
                    isPomodoro = false
                    isShortBreak = true
                    showDialog = true
                }
            }
            if (isShortBreak == true) {
                if (isRunning.value && remainingTimeShortBreak > 0 && !isPaused.value) {
                    remainingTimeShortBreak -= 1000
                } else if (remainingTimeShortBreak == 0L) {
                    isShortBreak = false
                    isPomodoro = true
                    showDialog = true
                    sessionCount=+1
                }
            }
            if (isLongBreak == true) {
                if (isRunning.value && remainingTimeLongBreak > 0 && !isPaused.value) {
                    remainingTimeLongBreak -= 1000
                } else if (remainingTimeLongBreak == 0L) {
                    isLongBreak = false
                    isPomodoro = true
                    showDialog = true
                    sessionCount=1
                }
            }
        }
    }

    if (showDialog) {
        isRunning.value = false
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                isPaused.value = true
            },
            title = {
                if (isPomodoro == true){
                    Text("Selesai Istirahat  Lanjutkan Sesi Pomodoro ")
                } else if (isShortBreak == true){
                    Text("Session $sessionCount Completed!")
                } else if (isLongBreak == true){
                    Text("Session $sessionCount Completed!")
                }
                    },
            text = {
                //Text("Take a ${if (sessionCount == 3) "long" else "short"} break\nRemaining time: ${formatTime(remainingTime)}")
                /*if (sessionCount == 1){
                    Text(" Lanjutkan Istirahat Pendek Pertama")
                }else if  (sessionCount == 2){
                    Text("Lanjutkan Istirahat Pendek Kedua")
                }else if  (sessionCount == 3){
                    Text("Lanjutkan Istirahat Panjang")
                }else if  (sessionCount == 4){
                    Text("Lanjutkan Istirahat Panjang")
                }*/
                   Text("sesi ke $sessionCount")
            },
            confirmButton = {
                Button(onClick = {
                    /*if (sessionCount == 3) {
                        remainingTimePomodoro = 900L
                        durationPomodoro = remainingTimePomodoro
                    } else {
                        remainingTimePomodoro = 300L
                        durationPomodoro = remainingTimePomodoro
                    }*/




                            if (isPomodoro == true ){
                                showDialog = false
                                remainingTimePomodoro = durationPomodoro * 1000
                                isRunning.value = true
                            }else if(isShortBreak == true){
                                showDialog = false
                                remainingTimeShortBreak = durationShortBreak * 1000
                                isRunning.value = true
                            } else if(isLongBreak == true) {
                                isLongBreak = true
                                isRunning.value = true
                                showDialog = false
                                remainingTimeLongBreak = durationLongBreak * 1000
                            }




                }) {
                    Text("OK")
                }
            }
        )
    }
}



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
            if(isPomodoro == true) {
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
            if(isShortBreak == true) {

                CircularProgressBar(
                    progress = (durationShortBreak - remainingTimeShortBreak / 1000f) / durationShortBreak,
                    modifier = Modifier.size(400.dp),
                    strokeWidth = 35.dp,
                )
                Text(
                    text = formatTime(remainingTimeShortBreak),
                    fontSize = 50.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Bold
                )

            }

            if(isLongBreak == true) {
                CircularProgressBar(
                    progress = (durationLongBreak - remainingTimeLongBreak / 1000f) / durationLongBreak,
                    modifier = Modifier.size(400.dp),
                    strokeWidth = 35.dp,
                )

                Text(
                    text = formatTime(remainingTimeLongBreak),
                    fontSize = 50.sp,
                    color = MaterialTheme.colors.primaryVariant,
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

