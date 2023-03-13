package com.moa.pomodoroapps.ui.screen.timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moa.pomodoroapps.R.*

@Composable
fun TimerScreen(){
    var duration by remember { mutableStateOf(10L) }
    var remainingTime by remember { mutableStateOf(10L) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    val nameList = listOf("Mengerjakan Pr", "Membaca Buku", "Mendengar Podcast",)
    var selectedName by remember { mutableStateOf(nameList[0]) }


    Column(modifier = Modifier.fillMaxSize()) {
        CountDownTopBar(
            title = "Timer",
            onBackClick = { /* handle back button click */ },
            onAudioClick = { /* handle audio button click */ }
        )
        NameDropDownList(
            nameList = nameList,
            selectedName = selectedName,
            onNameSelected = { name ->
                selectedName = name
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        )
        {
            CircularProgressBar(
                progress = (duration - remainingTime / 1000f) / duration,
                modifier = Modifier.size(400.dp),
                strokeWidth = 35.dp,
            )
            Text(
                text = formatTime(remainingTime),
                fontSize = 50.sp,
                color = MaterialTheme.colors.primaryVariant,
                fontWeight = FontWeight.Bold
            )
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
                    onClick = { isPaused = true },
                    enabled = !isPaused
                ) {
                    Text(text = "PAUSE")
                }
                Button(
                    onClick = {
                        isPaused = false
                        isRunning = true
                    },
                    enabled = isPaused
                ) {
                    Text(text = "RESUME")
                }
                Button(
                    onClick = { isRunning = false },
                    enabled = !isPaused
                ) {
                    Text(text = "STOP")
                }
            }

        }


    }

}
@Composable
fun CountDownTopBar(
    title: String,
    onBackClick: () -> Unit,
    onAudioClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back button"
                )
            }
        },
        actions = {
            IconButton(onClick = onAudioClick) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Audio button"
                )
            }
        }
    )
}

@Composable
fun NameDropDownList(
    nameList: List<String>,
    selectedName: String,
    onNameSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = painterResource(drawable.ic_launcher_background)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = selectedName,
            modifier = Modifier
                .clickable(onClick = { expanded = true })
                .padding(16.dp)
                .background(Color.LightGray)
        )
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(icon, contentDescription = "Dropdown Icon")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            nameList.forEach { name ->
                DropdownMenuItem(
                    onClick = {
                        onNameSelected(name)
                        expanded = false
                    }
                ) {
                    Text(
                        text = name,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


private fun formatTime(time: Long): String {
    val milliseconds = time % 1000 / 10
    val seconds = time / 1000 % 60
    val minutes = time / 1000 / 60
    return "%02d:%02d".format(minutes, seconds)
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
