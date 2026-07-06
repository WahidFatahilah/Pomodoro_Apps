package com.xyz.pomotrack.presentation.ui.screen.Statistic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xyz.pomotrack.Data.PeriodProductivityStatsRow
import com.xyz.pomotrack.presentation.ui.screen.HOME.MainViewModel
import com.xyz.pomotrack.presentation.ui.theme.FontColor
import com.xyz.pomotrack.presentation.ui.theme.Heading_H1
import com.xyz.pomotrack.presentation.ui.theme.Heading_H2
import com.xyz.pomotrack.presentation.ui.theme.Ket_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_2
import com.xyz.pomotrack.presentation.ui.theme.backgroundColor
import com.xyz.pomotrack.presentation.ui.theme.textMuted
import kotlin.math.min

private enum class StatisticPeriod(val label: String) {
    Today("Hari ini"),
    Week("Minggu ini"),
    Month("Bulan ini")
}

@Composable
fun StatisticScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val emptyStats = PeriodProductivityStatsRow(
        completedTasks = 0,
        totalFocusSessions = 0,
        totalFocusSeconds = 0L,
        activeProjects = 0
    )
    val todayStats by viewModel.todayProductivityStats.collectAsState(initial = emptyStats)
    val weekStats by viewModel.weekProductivityStats.collectAsState(initial = emptyStats)
    var selectedPeriod by remember { mutableStateOf(StatisticPeriod.Today) }
    var loaded by remember { mutableStateOf(false) }
    val loadProgress by animateFloatAsState(
        targetValue = if (loaded) 1f else 0f,
        animationSpec = tween(durationMillis = 850)
    )

    LaunchedEffect(Unit) {
        loaded = true
    }

    val activeStats = when (selectedPeriod) {
        StatisticPeriod.Today -> todayStats
        StatisticPeriod.Week -> weekStats
        StatisticPeriod.Month -> weekStats
    }
    val focusMinutes = activeStats.focusMinutes()
    val animatedProjects = (activeStats.activeProjects * loadProgress).toInt()
    val animatedTasks = (activeStats.completedTasks * loadProgress).toInt()
    val animatedMinutes = (focusMinutes * loadProgress).toInt()
    val dailyProgress = if (activeStats.completedTasks == 0) {
        0f
    } else {
        (activeStats.completedTasks / (activeStats.completedTasks + activeStats.activeProjects + 1f)).coerceIn(0f, 1f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatisticHeaderCard(
            projectCount = animatedProjects,
            taskCount = animatedTasks,
            focusMinutes = animatedMinutes
        )

        AchievementStrip(
            bestPeriod = when (selectedPeriod) {
                StatisticPeriod.Today -> "Terbaru"
                StatisticPeriod.Week -> "Terbanyak"
                StatisticPeriod.Month -> "Bulan ini"
            },
            completedToday = (todayStats.completedTasks * loadProgress).toInt(),
            completedWeek = (weekStats.completedTasks * loadProgress).toInt()
        )

        StatisticTabs(
            selectedPeriod = selectedPeriod,
            onSelect = { selectedPeriod = it }
        )

        when (selectedPeriod) {
            StatisticPeriod.Today -> DailyStatisticContent(
                progress = dailyProgress * loadProgress,
                completedTasks = animatedTasks,
                focusMinutes = animatedMinutes
            )

            StatisticPeriod.Week -> WeeklyStatisticContent(
                stats = weekStats,
                loadProgress = loadProgress
            )

            StatisticPeriod.Month -> MonthlyStatisticContent(
                stats = weekStats,
                loadProgress = loadProgress
            )
        }

        Spacer(modifier = Modifier.height(78.dp))
    }
}

@Composable
private fun StatisticHeaderCard(
    projectCount: Int,
    taskCount: Int,
    focusMinutes: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Statistik",
                style = Heading_H2,
                color = MaterialTheme.colors.FontColor
            )
            Divider(
                modifier = Modifier.padding(top = 16.dp, bottom = 14.dp),
                color = Color(0xFFE1E1E1)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderMetric(
                    value = projectCount.toString(),
                    title = "Total Project",
                    subtitle = "Selesai",
                    color = StatisticYellow,
                    modifier = Modifier.weight(1f)
                )
                HeaderMetric(
                    value = taskCount.toString(),
                    title = "Total Tugas",
                    subtitle = "Selesai",
                    color = StatisticPurple,
                    modifier = Modifier.weight(1f)
                )
                HeaderMetric(
                    value = "${focusMinutes}m",
                    title = "Total Waktu",
                    subtitle = "Digunakan",
                    color = StatisticBlue,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HeaderMetric(
    value: String,
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = value,
            style = Heading_H1.copy(fontWeight = FontWeight.Bold),
            color = color
        )
        Text(text = title, style = Ket_1, color = MaterialTheme.colors.FontColor)
        Text(text = "$subtitle  ->", style = Ket_1, color = MaterialTheme.colors.FontColor)
    }
}

@Composable
private fun AchievementStrip(
    bestPeriod: String,
    completedToday: Int,
    completedWeek: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Pencapaian", style = Subtitle_2, color = MaterialTheme.colors.FontColor)
            Text(text = bestPeriod, style = Subtitle_2, color = StatisticPurple)
        }
        AchievementNumber(
            value = completedToday,
            caption = "Tugas selesai\nhari ini"
        )
        Spacer(modifier = Modifier.width(18.dp))
        AchievementNumber(
            value = completedWeek,
            caption = "Tugas selesai\nminggu ini"
        )
    }
}

@Composable
private fun AchievementNumber(value: Int, caption: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = value.toString(),
            style = Heading_H1.copy(fontWeight = FontWeight.Bold),
            color = StatisticRed
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(text = caption, style = Ket_1, color = MaterialTheme.colors.FontColor)
    }
}

@Composable
private fun StatisticTabs(
    selectedPeriod: StatisticPeriod,
    onSelect: (StatisticPeriod) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
        StatisticPeriod.values().forEach { period ->
            Column(
                modifier = Modifier.clickable { onSelect(period) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = period.label,
                    style = Subtitle_2,
                    color = if (period == selectedPeriod) StatisticPink else MaterialTheme.colors.textMuted
                )
                Box(
                    modifier = Modifier
                        .width(if (period == selectedPeriod) 58.dp else 0.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(if (period == selectedPeriod) StatisticPink else Color.Transparent)
                )
            }
        }
    }
}

@Composable
private fun DailyStatisticContent(
    progress: Float,
    completedTasks: Int,
    focusMinutes: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "Tugas Terselesaikan",
            style = Subtitle_1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.FontColor
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            DailyDonutChart(progress = progress)
            Text(
                text = "${(progress * 100).toInt()}%",
                style = Heading_H1.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.FontColor
            )
        }
        ActivityRow(
            title = "Tugas selesai",
            value = "$completedTasks tugas",
            progress = progress,
            color = StatisticPurple
        )
        ActivityRow(
            title = "Waktu fokus",
            value = "$focusMinutes menit",
            progress = (focusMinutes / 180f).coerceIn(0f, 1f),
            color = StatisticYellow
        )
    }
}

@Composable
private fun WeeklyStatisticContent(
    stats: PeriodProductivityStatsRow,
    loadProgress: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Text(
            text = "Aktivitas Minggu Ini",
            style = Subtitle_1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.FontColor
        )
        WeeklyBarChart(
            baseValue = stats.focusMinutes().coerceAtLeast(1),
            loadProgress = loadProgress
        )
        Text(
            text = "Aktivitasmu",
            style = Subtitle_1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.FontColor
        )
        ActivityRow(
            title = "Waktu digunakan minggu ini",
            value = "${stats.focusMinutes()} menit",
            progress = (stats.focusMinutes() / 420f).coerceIn(0f, 1f) * loadProgress,
            color = StatisticBlue
        )
        ActivityRow(
            title = "Tugas selesai minggu ini",
            value = "${stats.completedTasks} tugas",
            progress = (stats.completedTasks / 20f).coerceIn(0f, 1f) * loadProgress,
            color = StatisticPink
        )
    }
}

@Composable
private fun MonthlyStatisticContent(
    stats: PeriodProductivityStatsRow,
    loadProgress: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Ringkasan Bulan Ini",
            style = Subtitle_1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.FontColor
        )
        ActivityRow(
            title = "Estimasi fokus bulan ini",
            value = "${stats.focusMinutes()} menit",
            progress = (stats.focusMinutes() / 1200f).coerceIn(0f, 1f) * loadProgress,
            color = StatisticPurple
        )
        ActivityRow(
            title = "Tugas selesai",
            value = "${stats.completedTasks} tugas",
            progress = (stats.completedTasks / 40f).coerceIn(0f, 1f) * loadProgress,
            color = StatisticYellow
        )
    }
}

@Composable
private fun DailyDonutChart(progress: Float) {
    Canvas(modifier = Modifier.size(170.dp)) {
        val stroke = 34.dp.toPx()
        val diameter = min(size.width, size.height) - stroke
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val ringSize = androidx.compose.ui.geometry.Size(diameter, diameter)
        val safeProgress = progress.coerceIn(0f, 1f)

        drawArc(
            color = Color(0xFFF1F1F1),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Butt)
        )
        drawArc(
            color = StatisticPurple,
            startAngle = -90f,
            sweepAngle = 360f * safeProgress,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = StatisticBlue,
            startAngle = 150f,
            sweepAngle = 65f * safeProgress,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = StatisticYellow,
            startAngle = 120f,
            sweepAngle = 50f * safeProgress,
            useCenter = false,
            topLeft = topLeft,
            size = ringSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun WeeklyBarChart(
    baseValue: Int,
    loadProgress: Float
) {
    val labels = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
    val colors = listOf(
        StatisticPurple,
        StatisticYellow,
        StatisticBlue,
        StatisticPink,
        Color(0xFFFF8A61),
        Color(0xFF31C77A),
        StatisticRed
    )
    val values = labels.indices.map { index ->
        (((baseValue + 22) * (index + 2)) % 72 + 22).coerceIn(18, 92)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.height(170.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOf("3j", "2j", "1j").forEach { label ->
                Text(text = label, style = Ket_1, color = MaterialTheme.colors.textMuted)
            }
        }
        labels.forEachIndexed { index, label ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .width(18.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height((values[index] * loadProgress).dp)
                            .clip(CircleShape)
                            .background(colors[index])
                    )
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height((values[index] * 0.72f * loadProgress).dp)
                            .clip(CircleShape)
                            .background(colors[index].copy(alpha = 0.28f))
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = label, style = Ket_1, color = MaterialTheme.colors.textMuted, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun ActivityRow(
    title: String,
    value: String,
    progress: Float,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = Subtitle_2,
                color = MaterialTheme.colors.FontColor
            )
            Text(text = value, style = Ket_1, color = MaterialTheme.colors.textMuted)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(Color(0xFFF0F0F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(7.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(color)
            )
        }
    }
}

private fun PeriodProductivityStatsRow.focusMinutes(): Int {
    return ((totalFocusSeconds + 59L) / 60L).toInt()
}

private val StatisticPink = Color(0xFFFF4B78)
private val StatisticRed = Color(0xFFFF3B3B)
private val StatisticYellow = Color(0xFFFFBE55)
private val StatisticPurple = Color(0xFFA58AFF)
private val StatisticBlue = Color(0xFF38BEE8)
