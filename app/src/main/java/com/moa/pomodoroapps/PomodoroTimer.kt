import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.moa.pomodoroapps.R

@Composable
fun CountDownTimerPomo(

) {
var duration by remember { mutableStateOf(1500L) }
var namaKegiatan by remember { mutableStateOf("") }
var remainingTime by remember { mutableStateOf(10L) }
var isRunning by remember { mutableStateOf(mutableStateOf(false)) }
var isPaused by remember { mutableStateOf(mutableStateOf(false)) }
var showTimerDialog by remember { mutableStateOf(false) }
val nameList = listOf("Mengerjakan Pr", "Membaca Buku", "Mendengar Podcast",)
var selectedName by remember { mutableStateOf(nameList[0]) }
var showDialog by remember { mutableStateOf(false) }

Box(
modifier = Modifier.fillMaxSize(),
contentAlignment = Alignment.Center
) {
    if (isRunning.value) {
        TimerPomodoro(
            nameList = nameList,
            selectedName = selectedName,
            duration = duration,
            remainingTime = remainingTime,
            isPaused = isPaused,
            isRunning = isRunning,

            )

    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Image(
                painter = painterResource(id = R.drawable.splashscreen),
                contentDescription = "gambar splash screen",
            )*/
            OutlinedTextField(
                value = duration.toString(),
                onValueChange = { input ->
                    duration = if (input.isEmpty()) 0 else input.toLong()
                    remainingTime = duration * 1000
                },
                label = { Text("Duration in seconds") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isRunning.value = true
                    remainingTime = duration * 1000
                },
                //* modifier = Modifier.align(Alignment.BottomCenter)*//*
            ) {
                Text(text = "START")
            }
        }
    }
}

LaunchedEffect(Unit) {
    while (true) {
        delay(1000)

        if (isRunning.value && remainingTime > 0 && !isPaused.value) {

            remainingTime -= 1000

        } else if (remainingTime == 0L) {

            showDialog = true
        }

    }

}

if (showDialog) {
    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text("Timer finished!") },
        text = { Text("The timer has finished counting down.") },
        confirmButton = {
            Button(onClick = {  }) {
                Text("OK")
            }
        }
    )
}
}



@Composable
fun TimerPomodoro(
    nameList : List<String>,
    selectedName : String,
    duration: Long,
    remainingTime: Long,
    isPaused: MutableState<Boolean>,
    isRunning : MutableState<Boolean>,
){
    //PlayLocalAudio()
    Column(modifier = Modifier.fillMaxSize()) {
        /*CountDownTopBar(
            title = "Timer",
            onBackClick = { isRunning.value = false },
            onAudioClick = { *//* handle audio button click *//* }
        )
        NameDropDownList(
            nameList = nameList,
            selectedName = selectedName,
            onNameSelected = { selectedName
            }
        )*/
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
                    onClick = { isRunning.value = false },
                    enabled = !isPaused.value
                ) {
                    Text(text = "STOP")
                }
            }
        }
    }
}
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
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}

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