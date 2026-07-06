package com.moa.pomodoroapps.presentation.ui.screen.HOME.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_1
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_2
import kotlin.math.min

@Composable
fun Statistic(
    totalProject: Int,
    totalTask: Int,
    totalTaskDone: Int,
    focusMinutesUsed: Int,
    totalPercentage: Int
) {
    val safePercentage = totalPercentage.coerceIn(0, 100)
    val progress = safePercentage / 100f

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        backgroundColor = HomeCardPink,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                HomeMetricBox(
                    title = "Total Proyek",
                    value = "$totalProject Project",
                    modifier = Modifier.weight(1f)
                )
                HomeMetricBox(
                    title = "Total Tugas",
                    value = "$totalTask Task",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProgressTextBox(
                    totalTask = totalTask,
                    totalTaskDone = totalTaskDone,
                    focusMinutesUsed = focusMinutesUsed,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Box(
                    modifier = Modifier
                        .size(118.dp)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    HomeProgressRing(progress = progress)
                    Text(
                        text = "$safePercentage%",
                        style = Heading_H2,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeMetricBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(13.dp))
            .background(HomeCardLightPink)
            .padding(horizontal = 13.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = title, style = Heading_H2, color = Color.White)
        Text(text = value, style = Subtitle_2, color = Color.White)
    }
}

@Composable
private fun ProgressTextBox(
    totalTask: Int,
    totalTaskDone: Int,
    focusMinutesUsed: Int,
    modifier: Modifier = Modifier
) {
    val empty = totalTask == 0 && focusMinutesUsed == 0

    Column(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(13.dp))
            .background(HomeCardLightPink)
            .padding(horizontal = 13.dp, vertical = 13.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "Progress berjalan", style = Subtitle_1, color = Color.White)
        Text(
            text = if (empty) "kamu belum memulai," else "$focusMinutesUsed Menit digunakan",
            style = Subtitle_2,
            color = Color.White
        )
        Text(
            text = if (empty) "ayo tambahkan tugasmu" else "$totalTaskDone tugas telah selesai",
            style = Subtitle_2,
            color = Color.White
        )
    }
}

@Composable
private fun HomeProgressRing(progress: Float) {
    val safeProgress = progress.coerceIn(0f, 1f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val stroke = 13.dp.toPx()
        val diameter = min(size.width, size.height) - stroke
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val ringSize = androidx.compose.ui.geometry.Size(diameter, diameter)

        drawArc(
            color = Color.White,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = HomeProgressYellow,
            startAngle = -90f,
            sweepAngle = 360f * safeProgress,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        drawCircle(
            color = HomeCardLightPink,
            radius = (diameter / 2f) - stroke * 1.2f,
            center = Offset(size.width / 2f, size.height / 2f)
        )
        drawCircle(
            color = HomeCardPink,
            radius = (diameter / 2f) - stroke * 1.95f,
            center = Offset(size.width / 2f, size.height / 2f)
        )
    }
}

private val HomeCardPink = Color(0xFFF74678)
private val HomeCardLightPink = Color(0xFFFF7398)
private val HomeProgressYellow = Color(0xFFFFC55A)
