package com.moa.pomodoroapps.presentation.ui.screen.Pomodoro

import android.media.MediaPlayer
import androidx.compose.animation.core.repeatable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.screen.Project.loadLottie
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H1
import kotlinx.coroutines.delay

@Composable
fun PomodoroScreen(onBackPressed: () -> Unit, taskName : String, navController: NavController) {

    var durationPomodoro by remember { mutableStateOf(15L) }
    var durationShortBreak by remember { mutableStateOf(5L) }
    var durationLongBreakkk by remember { mutableStateOf(5L) }
    var remainingTimePomodoro by remember { mutableStateOf(10L) }
    var remainingTimeShortBreak by remember { mutableStateOf(10L) }
    var remainingTimeLongBreakkk by remember { mutableStateOf(10L) }
    var isRunning by remember { mutableStateOf(mutableStateOf(false)) }
    var isPaused by remember { mutableStateOf(mutableStateOf(false)) }
    val nameList = listOf("Mengerjakan Pr", "Membaca Buku", "Mendengar Podcast",)
    var selectedName by remember { mutableStateOf(nameList[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var sessionCount by remember { mutableStateOf(1) }
    var isPomodoro by remember { mutableStateOf(true) }
    var isShortBreak by remember { mutableStateOf(false) }
    var isLongBreakkk by remember { mutableStateOf(false) }
    var isRocketLaunch by remember { mutableStateOf(false) }
    var showDialogShortBreak by remember { mutableStateOf(false) }
    var showDialogShortBreakkk by remember { mutableStateOf(false) }
    var showDialogPomodoro by remember { mutableStateOf(false) }
    var showDialogFinished by remember { mutableStateOf(false)  }
    val sessionCountDefault by remember { mutableStateOf(1) }
    var pomodoroStart by remember {  mutableStateOf(false)  }
    var task by remember { mutableStateOf(taskName) }
    val project = task.substringAfter("project=").substringBefore(",")
    val title = task.substringAfter("title=").substringBefore(",")

    if(isRocketLaunch){
        LaunchedEffect(Unit){
            delay(4000)
            isRunning.value = true
            remainingTimePomodoro = durationPomodoro * 1000
            pomodoroStart = true
            isRocketLaunch=false
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            rockedLaunch()
        }

    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isRunning.value) {
            if(isPomodoro){
                TimerPomodoro(
                    durationPomodoro,
                    isPaused,
                    isRunning,
                    sessionCount,
                    {},
                    remainingTimePomodoro = remainingTimePomodoro,
                    project,
                    title,
                )
            }
            if(isShortBreak) {
                TimerShortBreak(
                    isPaused,
                    isRunning,
                    sessionCount,
                    {
                    },
                   durationShortBreak,
                   remainingTimeShortBreak,
                    project,
                    title
                )
            }
            if(isLongBreakkk) {
                TimerLongBreak(
                    isPaused = isPaused,
                    isRunning = isRunning,
                    sessionCount = sessionCount,
                    onSessionComplete = {},
                    durationLongBreakkk = durationLongBreakkk,
                    remainingTimeLongBreakkk = remainingTimeLongBreakkk,
                    project,
                    title
                )
            }
        } else {
            if (!pomodoroStart) {
                if (!isRocketLaunch){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(modifier = Modifier.widthIn(min = 200.dp , max = 400.dp).heightIn(min = 200.dp , max = 400.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            rockedStandby()
                        }

                        Column(modifier = Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(text = "Project : $project", style = Heading_H1, color = MaterialTheme.colors.FontColor)
                            Text(text = "Task : $title", style = Heading_H1, color = MaterialTheme.colors.FontColor)
                        }

                        OutlinedTextField(
                            value = durationPomodoro.toString(),
                            onValueChange = { input ->
                                durationPomodoro = if (input.isEmpty()) 0 else input.toLong()
                                remainingTimePomodoro = durationPomodoro * 60 * 1000
                            },
                            label = { Text("Duration Pomodoro in minutes") },
                            leadingIcon = {
                                Box(modifier = Modifier.size(24.dp)) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_work),
                                        contentDescription = "Project",
                                        tint = MaterialTheme.colors.FontColor,
                                    )
                                }
                            },
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
                                remainingTimeShortBreak = durationShortBreak * 60 * 1000
                            },
                            label = { Text("Duration Short Break in minutes") },leadingIcon = {
                                Box(modifier = Modifier.size(24.dp)) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.leg),
                                        contentDescription = "Project",
                                        tint = MaterialTheme.colors.FontColor,
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = durationLongBreakkk.toString(),
                            onValueChange = { input ->
                                durationLongBreakkk = if (input.isEmpty()) 0 else input.toLong()
                                remainingTimeLongBreakkk= durationLongBreakkk * 60 * 1000
                            },
                            label = { Text("Duration Long Break in minutes ") },
                            leadingIcon = {
                                Box(modifier = Modifier.size(24.dp)) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cofee),
                                        contentDescription = "Project",
                                        tint = MaterialTheme.colors.FontColor,
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                isRocketLaunch = true
                            }
                        ) {
                            Text(text = "START")
                        }
                    }
                }
            } else {
                Column( modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally) {

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
                   // playAlarm()
                    if(sessionCount == 3 ){
                        showDialogShortBreakkk= true
                        isLongBreakkk = true
                    }else {
                        showDialogShortBreak = true
                    }
                }
            }
            if (isShortBreak) {
                if (isRunning.value && remainingTimeShortBreak > 0 && !isPaused.value) {
                    remainingTimeShortBreak -= 1000
                } else if (remainingTimeShortBreak == 0L) {
                    showDialogPomodoro = true
                }
            }
            if (isLongBreakkk) {
                if (isRunning.value && remainingTimeLongBreakkk > 0 && !isPaused.value) {
                    remainingTimeLongBreakkk -= 1000
                } else if (remainingTimeLongBreakkk == 0L) {
                    showDialogFinished = true
                }
            }
        }
    }
    if (showDialogShortBreak){
        isRunning.value = false
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                isPaused.value = true
            },
            title = {
                Text("Selesai Sesi Pomodoro $sessionCount")
            },
            text = {
                   Text("Lanjutkan Untuk Istirahat Pendek ")
            },
            confirmButton = {
                Button(onClick = {
                    isPomodoro = false
                    isShortBreak = true
                    isLongBreakkk = false
                    isRunning.value = true
                    showDialogShortBreak = false
                    remainingTimeShortBreak = durationShortBreak * 1000
                }) {
                    Text("OK")
                }
            }
        )
    }

    if (showDialogShortBreakkk){
        isRunning.value = false
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                isPaused.value = true
            },
            title = {
                Text("Selesai Pomodoro sesi $sessionCount")
            },
            text = {
                Text("Lanjutkan Untuk Istirahat Panjang")
            },
            confirmButton = {
                Button(onClick = {
                    isPomodoro = false
                    isShortBreak = false
                    isLongBreakkk = true
                    isRunning.value = true
                    showDialogShortBreakkk = false
                    remainingTimeLongBreakkk = durationLongBreakkk * 1000
                }) {
                    Text("OK")
                }
            }
        )
    }
    if (showDialogPomodoro){
        isRunning.value = false
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                isPaused.value = true
            },
            title = {
                Text("Istirahat Selesai")
            },
            text = {
                Text("Lanjutkan Untuk Sesi Pomodoro Berikutnya")
            },
            confirmButton = {
                Button(onClick = {
                    isPomodoro = true
                    isShortBreak = false
                    isLongBreakkk = false
                    isRunning.value = true
                    showDialogPomodoro = false
                    if(sessionCount == 3) {}
                    else {sessionCount++}
                    remainingTimePomodoro = durationPomodoro* 1000
                }) {
                    Text("OK")
                }
            }
        )
    }


    if (showDialogFinished){
        isRunning.value = false
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                showDialogFinished = false
                isRunning.value = false
            },
            title = {
                Text("Sesi Pomodoro Telah Berakhir")
            },
            text = {
                Text("Klik OK untuk mengulangi sesi dari awal")
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    isRunning.value = false
                    showDialogFinished = false
                    pomodoroStart = false
                }) {

                    Text("Kembali Ke Menu")
                }
            },
            confirmButton = {
                Button(onClick = {
                    isPomodoro = true
                    isShortBreak = false
                    isLongBreakkk = false
                    showDialogFinished = false
                    sessionCount = sessionCountDefault
                    remainingTimePomodoro = durationPomodoro* 1000
                    isRunning.value = true
                }) {
                    Text("Ulangi Sesi Pomodoro")
                }
            }
        )
    }
}

@Composable
fun playAlarm() {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.alarm).apply {
            setOnCompletionListener {
                // Restart the audio when it's finished
                this.start()
            }
        }
    }


    DisposableEffect(Unit) {
        mediaPlayer.start()
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}

@Composable
fun rockedStandby() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rocked_standby))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)
    LottieAnimation(composition = composition, progress = {progress})
}

@Composable
fun rockedLaunch() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rocket_launch))
    val progress by animateLottieCompositionAsState(composition = composition)
    LottieAnimation(composition = composition, progress = {progress})
}