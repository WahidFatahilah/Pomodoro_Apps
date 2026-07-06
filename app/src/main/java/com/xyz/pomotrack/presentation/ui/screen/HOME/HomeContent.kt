package com.xyz.pomotrack.presentation.ui.screen.HOME

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xyz.pomotrack.Data.TaskStatus
import com.xyz.pomotrack.ads.AdMobConfig
import com.xyz.pomotrack.presentation.navigation.topnav.CustomTopAppBar
import com.xyz.pomotrack.presentation.ui.screen.HOME.widget.EmptyTaskAndProject
import com.xyz.pomotrack.presentation.ui.screen.HOME.widget.Statistic
import com.xyz.pomotrack.presentation.ui.screen.HOME.widget.TaskListWithProject
import com.xyz.pomotrack.presentation.ui.theme.FontColor
import com.xyz.pomotrack.presentation.ui.theme.Heading_H1
import com.xyz.pomotrack.presentation.ui.theme.Ket_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_2
import com.xyz.pomotrack.presentation.ui.theme.backgroundColor
import com.xyz.pomotrack.presentation.ui.theme.focusAccent
import com.xyz.pomotrack.presentation.ui.theme.textMuted
import com.xyz.pomotrack.todo.components.EditDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeContent(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {
    val tasks by viewModel.homeTasks.collectAsState(initial = emptyList())
    val todayStats by viewModel.todayProductivityStats.collectAsState(initial = null)
    val totalTask = tasks.size
    val totalTaskDone = tasks.count { it.status == TaskStatus.DONE }
    val totalProject = tasks.map { it.projectId }.distinct().size
    val focusMinutesUsed = ((todayStats?.totalFocusSeconds ?: 0L) / 60L).toInt()
    val totalEstimatedMinutes = tasks.fold(0) { total, task ->
        total + task.estimatedMinutes.coerceAtLeast(1)
    }
    val totalPercentage = if (totalEstimatedMinutes == 0) {
        0
    } else {
        ((focusMinutesUsed.toFloat() / totalEstimatedMinutes.toFloat()) * 100).toInt().coerceIn(0, 100)
    }
    val visibleTasks = if (viewModel.showTaskDone) {
        tasks.filter { it.status == TaskStatus.DONE }
    } else {
        tasks.filter { it.status != TaskStatus.DONE }
    }
    val activeProjectCount = visibleTasks.map { it.projectId }.distinct().size

    if (viewModel.isShowDialog) {
        EditDialog(viewModel = viewModel)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            CustomTopAppBar()

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Proyek Hari ini",
                style = Heading_H1,
                color = MaterialTheme.colors.FontColor
            )

            Statistic(
                totalProject = totalProject,
                totalTask = totalTask,
                totalTaskDone = totalTaskDone,
                focusMinutesUsed = focusMinutesUsed,
                totalPercentage = totalPercentage
            )

            Spacer(modifier = Modifier.height(16.dp))
            BannerAdHome()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Tugas",
                        style = Heading_H1,
                        color = MaterialTheme.colors.FontColor
                    )
                    Text(
                        text = if (activeProjectCount == 0) {
                            "Belum ada task di schema baru."
                        } else {
                            "$activeProjectCount project aktif siap difokuskan."
                        },
                        style = Ket_1,
                        color = MaterialTheme.colors.textMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TaskFilter(
                showDone = viewModel.showTaskDone,
                activeCount = tasks.count { it.status != TaskStatus.DONE },
                doneCount = totalTaskDone,
                onShowActive = { viewModel.showTaskDone = false },
                onShowDone = { viewModel.showTaskDone = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (visibleTasks.isEmpty()) {
                EmptyTaskAndProject()
            } else {
                TaskListWithProject(
                    tasks = visibleTasks,
                    onClickRow = {
                        viewModel.setEditingTask(it)
                        viewModel.isShowDialog = true
                    },
                    onClickPlayPomo = { task ->
                        navController.navigate("pomodoro/${task.taskId}")
                    },
                    onClickDone = {
                        viewModel.CheckBoxDone(it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(88.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 78.dp)
                .size(56.dp)
                .shadow(8.dp, RoundedCornerShape(28.dp), clip = false)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colors.primary)
                .clickable { navController.navigate("addTask") },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(22.dp)) {
                val stroke = 2.4.dp.toPx()
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height * 0.22f),
                    end = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height * 0.78f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.White,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.22f, size.height / 2f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.78f, size.height / 2f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun BannerAdHome() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = AdMobConfig.BANNER_HOME_AD_UNIT_ID
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
private fun TaskFilter(
    showDone: Boolean,
    activeCount: Int,
    doneCount: Int,
    onShowActive: () -> Unit,
    onShowDone: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        FilterTab(
            text = "Hari ini",
            count = activeCount,
            selected = !showDone,
            onClick = onShowActive
        )
        FilterTab(
            text = "Selesai",
            count = doneCount,
            selected = showDone,
            onClick = onShowDone
        )
    }
}

@Composable
private fun FilterTab(
    text: String,
    count: Int,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$text $count",
            style = Subtitle_2,
            color = if (selected) MaterialTheme.colors.focusAccent else Color(0xFFB8B8B8)
        )
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(if (selected) MaterialTheme.colors.focusAccent else Color.Transparent)
        )
    }
}
