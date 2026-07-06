package com.moa.pomodoroapps.presentation.ui.screen.Pomodoro

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moa.pomodoroapps.Data.FocusSessionType
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H1
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Ket_1
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_1
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_2
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor
import com.moa.pomodoroapps.presentation.ui.theme.breakAccent
import com.moa.pomodoroapps.presentation.ui.theme.focusAccent
import com.moa.pomodoroapps.presentation.ui.theme.surfaceElevated
import com.moa.pomodoroapps.presentation.ui.theme.surfaceMuted
import com.moa.pomodoroapps.presentation.ui.theme.textMuted
import com.moa.pomodoroapps.presentation.ui.theme.warningAccent
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private enum class PomodoroMode(
    val label: String,
    val action: String,
    val completeTitle: String,
    val completeMessage: String
) {
    Focus(
        label = "Focus",
        action = "Start Short Break",
        completeTitle = "Focus session complete",
        completeMessage = "Nice work. Take a short break before the next round."
    ),
    ShortBreak(
        label = "Short Break",
        action = "Start Focus",
        completeTitle = "Break complete",
        completeMessage = "Ready to continue the next focus session."
    ),
    LongBreak(
        label = "Long Break",
        action = "Finish",
        completeTitle = "Pomodoro cycle complete",
        completeMessage = "You completed the full focus cycle."
    )
}

@Composable
fun PomodoroScreen(
    onBackPressed: () -> Unit,
    taskId: Long,
    viewModel: PomodoroViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val taskDetail by viewModel.taskDetail.collectAsState(initial = null)
    var focusMinutes by remember { mutableStateOf("25") }
    var shortBreakMinutes by remember { mutableStateOf("5") }
    var longBreakMinutes by remember { mutableStateOf("15") }
    var mode by remember { mutableStateOf(PomodoroMode.Focus) }
    var sessionCount by remember { mutableStateOf(1) }
    var isStarted by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var notificationPermissionRequested by remember { mutableStateOf(false) }
    var projectMenuExpanded by remember { mutableStateOf(false) }
    var soundEnabled by remember { mutableStateOf(true) }
    val remainingTime = viewModel.remainingTimeMillis.takeIf { isStarted } ?: focusMinutes.toDurationMillis()
    val totalTime = viewModel.totalTimeMillis.takeIf { isStarted } ?: focusMinutes.toDurationMillis()
    val isRunning = viewModel.isTimerRunning
    val isPaused = viewModel.isTimerPaused

    val project = taskDetail?.projectName ?: "Focus Project"
    val title = taskDetail?.title ?: if (taskId > 0L) "Loading task..." else "Focus Session"
    val timerStatusText = if (isPaused) "Paused" else if (isRunning) "In progress" else "Ready"

    fun cancelAndExit() {
        if (isStarted && viewModel.currentSessionId != null && remainingTime < totalTime && remainingTime > 0L) {
            viewModel.cancelCurrentSession((totalTime - remainingTime).coerceAtLeast(0L))
        }
        context.cancelPomodoroNotification()
        isStarted = false
        onBackPressed()
    }

    fun requestExit() {
        if (isStarted && remainingTime > 0L) {
            if (!showExitDialog) {
                showExitDialog = true
            }
        } else {
            cancelAndExit()
        }
    }

    BackHandler(enabled = isStarted && remainingTime > 0L) {
        if (!showExitDialog) {
            showExitDialog = true
        }
    }

    LaunchedEffect(taskDetail?.estimatedMinutes, isStarted) {
        if (!isStarted) {
            val configuredMinutes = taskDetail?.estimatedMinutes ?: 25
            focusMinutes = configuredMinutes.toString()
        }
    }

    LaunchedEffect(isStarted, isRunning, remainingTime) {
        if (isStarted && !isRunning && remainingTime <= 0L) {
            showCompletionDialog = true
            isStarted = false
        }
    }

    LaunchedEffect(isStarted, remainingTime, mode, title, timerStatusText) {
        if (isStarted) {
            if (!notificationPermissionRequested) {
                context.requestPomodoroNotificationPermissionIfNeeded()
                notificationPermissionRequested = true
            }
            context.showPomodoroNotification(
                title = title,
                mode = mode.label,
                time = remainingTime.formatTime(),
                status = timerStatusText
            )
        } else {
            notificationPermissionRequested = false
            context.cancelPomodoroNotification()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            context.cancelPomodoroNotification()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TimerTopBar(
            soundEnabled = soundEnabled,
            onBackPressed = ::requestExit,
            onSoundToggle = { soundEnabled = !soundEnabled }
        )

        ProjectSelectorCard(
            project = project,
            sessionCount = sessionCount,
            expanded = projectMenuExpanded,
            onToggle = { projectMenuExpanded = !projectMenuExpanded }
        )

        if (!isStarted) {
            SetupCard(
                focusMinutes = focusMinutes,
                shortBreakMinutes = shortBreakMinutes,
                longBreakMinutes = longBreakMinutes,
                onFocusChange = { focusMinutes = it.onlyDigits() },
                onShortBreakChange = { shortBreakMinutes = it.onlyDigits() },
                onLongBreakChange = { longBreakMinutes = it.onlyDigits() },
                onStart = {
                    mode = PomodoroMode.Focus
                    sessionCount = 1
                    isStarted = true
                    viewModel.startTimer(
                        sessionType = PomodoroMode.Focus.toSessionType(),
                        plannedMillis = focusMinutes.toDurationMillis()
                    )
                }
            )
        } else {
            FocusTimerCard(
                mode = mode,
                title = title,
                sessionCount = sessionCount,
                remainingTime = remainingTime,
                totalTime = totalTime,
                isPaused = isPaused,
                isRunning = isRunning,
                onPauseResume = {
                    if (isPaused) {
                        viewModel.resumeTimer()
                    } else {
                        viewModel.pauseTimer()
                    }
                },
                onStop = {
                    if (viewModel.currentSessionId != null && remainingTime < totalTime && remainingTime > 0L) {
                        viewModel.cancelCurrentSession((totalTime - remainingTime).coerceAtLeast(0L))
                    }
                    context.cancelPomodoroNotification()
                    isStarted = false
                    mode = PomodoroMode.Focus
                    sessionCount = 1
                    viewModel.stopTimer()
                }
            )
        }

        Spacer(modifier = Modifier.height(76.dp))
    }

    if (showCompletionDialog) {
        SessionCompleteDialog(
            mode = mode,
            onDismiss = { showCompletionDialog = false },
            onConfirm = {
                showCompletionDialog = false
                when (mode) {
                    PomodoroMode.Focus -> {
                        val nextMode = if (sessionCount >= 3) PomodoroMode.LongBreak else PomodoroMode.ShortBreak
                        val nextMinutes = if (sessionCount >= 3) {
                            longBreakMinutes.toDurationMinutes()
                        } else {
                            shortBreakMinutes.toDurationMinutes()
                        }
                        mode = nextMode
                        isStarted = true
                        viewModel.startTimer(nextMode.toSessionType(), nextMinutes * 60_000L)
                    }

                    PomodoroMode.ShortBreak -> {
                        sessionCount = (sessionCount + 1).coerceAtMost(3)
                        mode = PomodoroMode.Focus
                        val nextMinutes = focusMinutes.toDurationMinutes()
                        isStarted = true
                        viewModel.startTimer(PomodoroMode.Focus.toSessionType(), nextMinutes * 60_000L)
                    }

                    PomodoroMode.LongBreak -> {
                        isStarted = false
                        mode = PomodoroMode.Focus
                        sessionCount = 1
                        viewModel.stopTimer()
                        context.cancelPomodoroNotification()
                    }
                }
            }
        )
    }

    if (showExitDialog) {
        ExitSessionDialog(
            onDismiss = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                cancelAndExit()
            }
        )
    }
}

@Composable
private fun TimerTopBar(
    soundEnabled: Boolean,
    onBackPressed: () -> Unit,
    onSoundToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = MaterialTheme.colors.FontColor
            )
        }
        Text(
            text = "Timer",
            modifier = Modifier.weight(1f),
            style = Heading_H2,
            color = MaterialTheme.colors.FontColor,
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = onSoundToggle,
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Icon(
                painter = painterResource(id = if (soundEnabled) R.drawable.ic_sound_on else R.drawable.ic_sound_off),
                contentDescription = if (soundEnabled) "Sound on" else "Sound off",
                tint = MaterialTheme.colors.FontColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    Divider(color = Color(0xFFD8D8D8), thickness = 1.dp)
}

@Composable
private fun ProjectSelectorCard(
    project: String,
    sessionCount: Int,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colors.surfaceElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clickable(onClick = onToggle)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = project, style = Subtitle_2, color = MaterialTheme.colors.FontColor)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "$sessionCount/3", style = Ket_1, color = Color(0xFFC7C7C7))
            Spacer(modifier = Modifier.width(12.dp))
            ProjectChevron(expanded = expanded, modifier = Modifier.size(18.dp))
        }

        if (expanded) {
            Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            Text(
                text = "Proyek 2",
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                style = Subtitle_2,
                color = MaterialTheme.colors.FontColor
            )
        }
    }
}

@Composable
private fun ProjectChevron(expanded: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.2.dp.toPx()
        val yStart = if (expanded) size.height * 0.62f else size.height * 0.38f
        val yEnd = if (expanded) size.height * 0.38f else size.height * 0.62f
        drawLine(
            color = Color(0xFF252525),
            start = Offset(size.width * 0.22f, yStart),
            end = Offset(size.width * 0.5f, yEnd),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF252525),
            start = Offset(size.width * 0.78f, yStart),
            end = Offset(size.width * 0.5f, yEnd),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun SetupCard(
    focusMinutes: String,
    shortBreakMinutes: String,
    longBreakMinutes: String,
    onFocusChange: (String) -> Unit,
    onShortBreakChange: (String) -> Unit,
    onLongBreakChange: (String) -> Unit,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = MaterialTheme.colors.surfaceElevated,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colors.surfaceMuted.copy(alpha = 0.46f)),
                contentAlignment = Alignment.Center
            ) {
                rockedStandby(modifier = Modifier.size(124.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "Prepare focus session", style = Heading_H2, color = MaterialTheme.colors.FontColor)
                Text(text = "Set the rhythm before starting the timer.", style = Ket_1, color = MaterialTheme.colors.textMuted)
            }

            DurationOptionCard(
                title = "Focus",
                subtitle = "Deep work timer",
                value = focusMinutes,
                icon = R.drawable.ic_work,
                tint = MaterialTheme.colors.focusAccent,
                onValueChange = onFocusChange
            )
            DurationOptionCard(
                title = "Short Break",
                subtitle = "Quick reset",
                value = shortBreakMinutes,
                icon = R.drawable.leg,
                tint = MaterialTheme.colors.breakAccent,
                onValueChange = onShortBreakChange
            )
            DurationOptionCard(
                title = "Long Break",
                subtitle = "Recovery after 3 sessions",
                value = longBreakMinutes,
                icon = R.drawable.ic_cofee,
                tint = MaterialTheme.colors.warningAccent,
                onValueChange = onLongBreakChange
            )

            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.focusAccent),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(text = "Start Focus", style = Subtitle_1, color = Color.White)
            }
        }
    }
}

@Composable
private fun DurationOptionCard(
    title: String,
    subtitle: String,
    value: String,
    icon: Int,
    tint: Color,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colors.surfaceMuted.copy(alpha = 0.72f))
            .border(1.dp, tint.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = title,
                tint = tint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = title, style = Subtitle_1, color = MaterialTheme.colors.FontColor)
            Text(text = subtitle, style = Ket_1, color = MaterialTheme.colors.textMuted)
        }

        Spacer(modifier = Modifier.width(10.dp))

        CompactMinuteField(
            value = value,
            tint = tint,
            onValueChange = onValueChange
        )
    }
}

@Composable
private fun CompactMinuteField(
    value: String,
    tint: Color,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.width(78.dp),
        textStyle = Subtitle_1.copy(textAlign = TextAlign.Center),
        trailingIcon = {
            Text(text = "m", style = Ket_1, color = MaterialTheme.colors.textMuted)
        },
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = tint,
            unfocusedBorderColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.surfaceElevated,
            textColor = MaterialTheme.colors.FontColor,
            cursorColor = tint
        )
    )
}

@Composable
private fun FocusTimerCard(
    mode: PomodoroMode,
    title: String,
    sessionCount: Int,
    remainingTime: Long,
    totalTime: Long,
    isPaused: Boolean,
    isRunning: Boolean,
    onPauseResume: () -> Unit,
    onStop: () -> Unit
) {
    val accent = if (mode == PomodoroMode.Focus) FigmaFocusRed else FigmaBreakYellow
    val progress = if (totalTime <= 0L) 0f else remainingTime.toFloat() / totalTime.toFloat()
    val isBreak = mode != PomodoroMode.Focus

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(268.dp),
            contentAlignment = Alignment.Center
        ) {
            TimerProgressRing(progress = progress, color = accent)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = remainingTime.formatTime(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.FontColor
                )
                TimerCenterDots(sessionCount = sessionCount, accent = accent)
            }
        }

        Spacer(modifier = Modifier.height(34.dp))

        Text(
            text = if (isBreak) "Istirahat" else title,
            style = Subtitle_1.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colors.FontColor
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (isBreak) "${(totalTime / 60_000L).coerceAtLeast(1L)} Menit" else "Sesi $sessionCount/3",
            style = Ket_1,
            color = Color(0xFFC7C7C7)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FigmaCircleButton(
                onClick = onPauseResume,
                color = accent,
                icon = if (isPaused) TimerButtonIcon.Play else TimerButtonIcon.Pause,
                buttonSize = 72.dp,
                iconColor = Color.White
            )
            Spacer(modifier = Modifier.width(30.dp))
            FigmaCircleButton(
                onClick = onStop,
                color = Color.White,
                icon = if (isBreak) TimerButtonIcon.Next else TimerButtonIcon.Stop,
                buttonSize = 64.dp,
                iconColor = accent
            )
        }

        Spacer(modifier = Modifier.height(36.dp))
        Text(text = "Ringtone 1", style = Ket_1, color = Color(0xFFC7C7C7))
    }
}

private enum class TimerButtonIcon {
    Play,
    Pause,
    Stop,
    Next
}

private val FigmaFocusRed = Color(0xFFF64B78)
private val FigmaBreakYellow = Color(0xFFF6BA4F)

@Composable
private fun ModeChip(mode: PomodoroMode, sessionCount: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (mode == PomodoroMode.Focus) {
                    MaterialTheme.colors.focusAccent.copy(alpha = 0.11f)
                } else {
                    MaterialTheme.colors.breakAccent.copy(alpha = 0.12f)
                }
            )
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val accent = if (mode == PomodoroMode.Focus) MaterialTheme.colors.focusAccent else MaterialTheme.colors.breakAccent
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accent)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "${mode.label} - Session $sessionCount/3", style = Subtitle_2, color = MaterialTheme.colors.FontColor)
    }
}

@Composable
private fun TimerStatusText(text: String) {
    Text(
        text = text,
        style = Ket_1,
        color = MaterialTheme.colors.textMuted
    )
}

@Composable
private fun SessionProgressSummary(sessionCount: Int, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colors.surfaceMuted.copy(alpha = 0.52f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Cycle progress", style = Ket_1, color = MaterialTheme.colors.textMuted)
        Spacer(modifier = Modifier.weight(1f))
        SessionDots(sessionCount = sessionCount, accent = accent)
    }
}

@Composable
private fun SessionDots(sessionCount: Int, accent: Color = MaterialTheme.colors.focusAccent) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        for (index in 1..3) {
            Box(
                modifier = Modifier
                    .size(width = if (index == sessionCount) 26.dp else 9.dp, height = 9.dp)
                    .clip(CircleShape)
                    .background(
                        if (index <= sessionCount) accent else MaterialTheme.colors.surfaceElevated
                    )
            )
        }
    }
}

@Composable
private fun TimerCenterDots(sessionCount: Int, accent: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (index in 1..3) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (index <= sessionCount) accent else Color.Transparent
                    )
                    .border(
                        width = 1.dp,
                        color = if (index <= sessionCount) accent else Color(0xFFD7D7D7),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun FigmaCircleButton(
    onClick: () -> Unit,
    color: Color,
    icon: TimerButtonIcon,
    buttonSize: Dp,
    iconColor: Color
) {
    Box(
        modifier = Modifier
            .size(buttonSize)
            .shadow(16.dp, CircleShape, clip = false)
            .clip(CircleShape)
            .background(color)
            .border(6.dp, Color.White, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size((buttonSize.value * 0.44f).dp)) {
            when (icon) {
                TimerButtonIcon.Play -> {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width * 0.28f, size.height * 0.16f)
                        lineTo(size.width * 0.28f, size.height * 0.84f)
                        lineTo(size.width * 0.84f, size.height * 0.50f)
                        close()
                    }
                    drawPath(path = path, color = iconColor)
                }

                TimerButtonIcon.Pause -> {
                    val radius = 1.4.dp.toPx()
                    drawRoundRect(
                        color = iconColor,
                        topLeft = Offset(size.width * 0.22f, size.height * 0.13f),
                        size = androidx.compose.ui.geometry.Size(size.width * 0.20f, size.height * 0.74f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius)
                    )
                    drawRoundRect(
                        color = iconColor,
                        topLeft = Offset(size.width * 0.58f, size.height * 0.13f),
                        size = androidx.compose.ui.geometry.Size(size.width * 0.20f, size.height * 0.74f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius)
                    )
                }

                TimerButtonIcon.Stop -> {
                    drawRect(
                        color = iconColor,
                        topLeft = Offset(size.width * 0.28f, size.height * 0.28f),
                        size = androidx.compose.ui.geometry.Size(size.width * 0.44f, size.height * 0.44f)
                    )
                }

                TimerButtonIcon.Next -> {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width * 0.18f, size.height * 0.20f)
                        lineTo(size.width * 0.18f, size.height * 0.80f)
                        lineTo(size.width * 0.66f, size.height * 0.50f)
                        close()
                    }
                    drawPath(path = path, color = iconColor)
                    drawRect(
                        color = iconColor,
                        topLeft = Offset(size.width * 0.76f, size.height * 0.20f),
                        size = androidx.compose.ui.geometry.Size(size.width * 0.08f, size.height * 0.60f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PrimaryTimerButton(
    onClick: () -> Unit,
    text: String,
    icon: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = Subtitle_2, color = Color.White)
    }
}

@Composable
private fun SecondaryTimerButton(
    onClick: () -> Unit,
    text: String,
    icon: Int,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MaterialTheme.colors.surfaceElevated,
            contentColor = MaterialTheme.colors.textMuted
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = MaterialTheme.colors.textMuted,
            modifier = Modifier.size(19.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = Subtitle_2, color = MaterialTheme.colors.textMuted)
    }
}

@Composable
private fun TimerProgressRing(progress: Float, color: Color) {
    val trackColor = Color(0xFFF5F5F5)
    Canvas(modifier = Modifier.fillMaxSize()) {
        val stroke = 24.dp.toPx()
        val diameter = min(size.width, size.height) - stroke * 1.6f
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val ringSize = androidx.compose.ui.geometry.Size(diameter, diameter)
        val safeProgress = progress.coerceIn(0f, 1f)

        drawArc(
            color = color.copy(alpha = 0.08f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft.copy(y = topLeft.y + 10.dp.toPx()),
            size = ringSize,
            style = Stroke(width = stroke * 1.18f, cap = StrokeCap.Round)
        )
        drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * safeProgress,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        val angle = Math.toRadians((-90f + 360f * safeProgress).toDouble())
        val radius = diameter / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        val dotCenter = Offset(
            x = center.x + cos(angle).toFloat() * radius,
            y = center.y + sin(angle).toFloat() * radius
        )
        drawCircle(
            color = Color.White,
            radius = 10.dp.toPx(),
            center = dotCenter
        )
    }
}

@Composable
private fun SessionCompleteDialog(mode: PomodoroMode, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = mode.completeTitle) },
        text = { Text(text = mode.completeMessage) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.focusAccent)
            ) {
                Text(text = mode.action, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = "Later")
            }
        }
    )
}

@Composable
private fun ExitSessionDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Batalkan sesi?") },
        text = { Text(text = "Countdown yang sedang berjalan akan dibatalkan jika keluar dari layar ini.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.focusAccent)
            ) {
                Text(text = "Keluar", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = "Tetap di sini")
            }
        }
    )
}

private const val POMODORO_NOTIFICATION_ID = 2510
private const val POMODORO_CHANNEL_ID = "pomodoro_timer_channel"
private const val POMODORO_NOTIFICATION_PERMISSION_REQUEST = 2511

private fun Context.requestPomodoroNotificationPermissionIfNeeded() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return

    val activity = findActivity() ?: return
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        POMODORO_NOTIFICATION_PERMISSION_REQUEST
    )
}

private fun Context.showPomodoroNotification(
    title: String,
    mode: String,
    time: String,
    status: String
) {
    createPomodoroNotificationChannel()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
    } ?: Intent()
    val pendingIntent = PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val notification = NotificationCompat.Builder(this, POMODORO_CHANNEL_ID)
        .setSmallIcon(R.drawable.timerlogo)
        .setContentTitle("$time - $mode")
        .setContentText("$title - $status")
        .setContentIntent(pendingIntent)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setShowWhen(false)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setCategory(NotificationCompat.CATEGORY_STATUS)
        .build()

    try {
        NotificationManagerCompat.from(this).notify(POMODORO_NOTIFICATION_ID, notification)
    } catch (_: SecurityException) {
        // Permission can be revoked while the timer is running.
    }
}

private fun Context.cancelPomodoroNotification() {
    NotificationManagerCompat.from(this).cancel(POMODORO_NOTIFICATION_ID)
}

private fun Context.createPomodoroNotificationChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val channel = NotificationChannel(
        POMODORO_CHANNEL_ID,
        "Pomodoro Timer",
        NotificationManager.IMPORTANCE_LOW
    ).apply {
        description = "Shows active Pomodoro countdown"
        setShowBadge(false)
    }
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is android.content.ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

@Composable
fun playAlarm() {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.alarm)
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
fun rockedStandby(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rocked_standby))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = composition != null,
        iterations = LottieConstants.IterateForever
    )

    if (composition == null) {
        Box(modifier = modifier)
    } else {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
        )
    }
}

@Composable
fun rockedLaunch(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rocket_launch))
    val progress by animateLottieCompositionAsState(composition = composition)
    LottieAnimation(composition = composition, progress = { progress }, modifier = modifier)
}

private fun PomodoroMode.toSessionType(): FocusSessionType {
    return when (this) {
        PomodoroMode.Focus -> FocusSessionType.FOCUS
        PomodoroMode.ShortBreak -> FocusSessionType.SHORT_BREAK
        PomodoroMode.LongBreak -> FocusSessionType.LONG_BREAK
    }
}

private fun String.onlyDigits(): String = filter { it.isDigit() }.take(3)

private fun String.toDurationMillis(): Long {
    val minutes = toLongOrNull()?.coerceAtLeast(1L) ?: 1L
    return minutes * 60_000L
}

private fun String.toDurationMinutes(): Int {
    return toIntOrNull()?.coerceAtLeast(1) ?: 1
}

private fun Long.formatTime(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
