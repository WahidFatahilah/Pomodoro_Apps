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
import com.moa.pomodoroapps.presentation.ui.screen.HOME.TimerPomodoro
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
    var showDialogShortBreak by remember { mutableStateOf(false) }
    var showDialogLongBreak by remember { mutableStateOf(false) }
    var showDialogPomodoro by remember { mutableStateOf(false) }
    var showDialogFinished by remember { mutableStateOf(false)  }
    var pomodoroStart by remember { mutableStateOf(false)  }
    val sessionCountDefault by remember { mutableStateOf(1) }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isRunning.value) {
            if(isPomodoro){
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

                    }
                )
            }
            if(isShortBreak){
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

                    }
                )
            }
            if(isLongBreak) {
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

                    }
                )
            }

        } else {
            if (pomodoroStart == false) {
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
                            remainingTimePomodoro = durationPomodoro * 60 * 1000
                        },
                        label = { Text("Duration Pomodoro in minutes") },
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
                        label = { Text("Duration Short Break in minutes") },
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
                            remainingTimeLongBreak = durationLongBreak * 60 * 1000
                        },
                        label = { Text("Duration Long Break in minutes ") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isRunning.value = true
                            remainingTimePomodoro = durationPomodoro * 1000 * 60
                            pomodoroStart = true
                        }
                    ) {
                        Text(text = "START")
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
                    if(sessionCount == 3 ){
                        showDialogLongBreak= true
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
            if (isLongBreak) {
                if (isRunning.value && remainingTimeLongBreak > 0 && !isPaused.value) {
                    remainingTimeLongBreak -= 1000
                } else if (remainingTimeLongBreak == 0L) {
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
                Text("Selesai Sesi Pomodoro 1")
            },
            text = {
                   Text("Lanjutkan Untuk Istirahat Pendek")
            },
            confirmButton = {
                Button(onClick = {
                    isPomodoro = false
                    isShortBreak = true
                    isLongBreak = false
                    isRunning.value = true
                    showDialogShortBreak = false
                    remainingTimeShortBreak = durationShortBreak * 1000
                }) {
                    Text("OK")
                }
            }
        )
    }
    if (showDialogShortBreak){
        isRunning.value = false
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                isPaused.value = true
            },
            title = {
                Text("Selesai Sesi Pomodoro 1")
            },
            text = {
                Text("Lanjutkan Untuk Istirahat Pendek")
            },
            confirmButton = {
                Button(onClick = {
                    isPomodoro = false
                    isShortBreak = true
                    isLongBreak = false
                    isRunning.value = true
                    showDialogShortBreak = false
                    remainingTimeShortBreak = durationShortBreak * 1000
                }) {
                    Text("OK")
                }
            }
        )
    }
    if (showDialogLongBreak){
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
                    isLongBreak = true
                    isRunning.value = true
                    showDialogLongBreak = false
                    remainingTimeLongBreak = durationLongBreak * 1000
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
                    isLongBreak = false
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
                isPaused.value = true
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
                }) {
                    pomodoroStart = false
                    Text("Kembali Ke Menu")
                }
            },
            confirmButton = {
                Button(onClick = {
                    isPomodoro = true
                    isShortBreak = false
                    isLongBreak = false
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

