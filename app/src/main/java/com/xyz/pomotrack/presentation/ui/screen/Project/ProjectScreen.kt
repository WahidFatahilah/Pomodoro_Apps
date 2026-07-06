package com.xyz.pomotrack.presentation.ui.screen.Project

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xyz.pomotrack.Data.ProjectWithTasks
import com.xyz.pomotrack.Data.TaskEntity
import com.xyz.pomotrack.Data.TaskStatus
import com.xyz.pomotrack.R
import com.xyz.pomotrack.presentation.ui.screen.HOME.MainViewModel
import com.xyz.pomotrack.presentation.ui.theme.FontColor
import com.xyz.pomotrack.presentation.ui.theme.Heading_H1
import com.xyz.pomotrack.presentation.ui.theme.Heading_H2
import com.xyz.pomotrack.presentation.ui.theme.Ket_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_2
import com.xyz.pomotrack.presentation.ui.theme.backgroundColor
import com.xyz.pomotrack.presentation.ui.theme.surfaceElevated
import com.xyz.pomotrack.presentation.ui.theme.textMuted
import com.xyz.pomotrack.todo.components.EditDialog
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Composable
fun ProjectScreen(
    navController: NavController,
    boardViewModel: ProjectBoardViewModel = hiltViewModel(),
    taskViewModel: MainViewModel = hiltViewModel()
) {
    val timerTask by taskViewModel.homeTasks.collectAsState(initial = emptyList())

    if (taskViewModel.isShowDialog) {
        EditDialog(viewModel = taskViewModel)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        ProjectScreenHeader()

        Divider(
            modifier = Modifier.padding(top = 18.dp),
            color = MaterialTheme.colors.textMuted.copy(alpha = 0.22f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        DeadlineTabs(
            selectedBucket = boardViewModel.selectedBucket,
            onSelectBucket = boardViewModel::selectBucket
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Project Anda",
            style = Heading_H2,
            color = MaterialTheme.colors.FontColor
        )

        Spacer(modifier = Modifier.height(14.dp))

        ProjectHorizontalList(
            projects = boardViewModel.filteredProjects,
            selectedProjectId = boardViewModel.selectedProject?.project?.id,
            onSelectProject = boardViewModel::selectProject
        )

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tugas Anda",
                style = Heading_H2,
                color = MaterialTheme.colors.FontColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = boardViewModel.selectedProjectProgressLabel,
                style = Subtitle_2,
                color = MaterialTheme.colors.textMuted
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "+",
                style = Heading_H2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable { navController.navigate("addTask") }
            )
            Spacer(modifier = Modifier.width(18.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = if (boardViewModel.isTaskSectionExpanded) "Collapse" else "Expand",
                tint = MaterialTheme.colors.FontColor,
                modifier = Modifier
                    .size(22.dp)
                    .rotate(if (boardViewModel.isTaskSectionExpanded) -90f else 90f)
                    .clickable { boardViewModel.toggleTaskSectionExpanded() }
            )
        }

        if (boardViewModel.isTaskSectionExpanded) {
            Spacer(modifier = Modifier.height(14.dp))

            if (boardViewModel.selectedProjectActiveTasks.isEmpty()) {
                Text(
                    text = "Belum ada tugas aktif pada periode ini.",
                    style = Ket_1,
                    color = MaterialTheme.colors.textMuted
                )
            } else {
                boardViewModel.selectedProjectActiveTasks.forEach { task ->
                    ProjectTaskCard(
                        task = task,
                        onEdit = {
                            val row = timerTask.firstOrNull { it.taskId == task.id }
                            if (row != null) {
                                taskViewModel.setEditingTask(row)
                                taskViewModel.isShowDialog = true
                            }
                        },
                        onPlay = { navController.navigate("pomodoro/${task.id}") },
                        onToggleDone = {
                            val row = timerTask.firstOrNull { it.taskId == task.id }
                            if (row != null) {
                                taskViewModel.CheckBoxDone(row)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Tugas Selesai",
            style = Heading_H2,
            color = MaterialTheme.colors.FontColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (boardViewModel.selectedProjectCompletedTasks.isEmpty()) {
            Text(
                text = "Belum ada tugas selesai pada periode ini.",
                style = Ket_1,
                color = MaterialTheme.colors.textMuted
            )
        } else {
            boardViewModel.selectedProjectCompletedTasks.forEach { task ->
                CompletedTaskRow(task = task)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
private fun ProjectScreenHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(40.dp))

        Text(
            text = "Proyek Saya",
            modifier = Modifier.weight(1f),
            style = Heading_H2,
            color = MaterialTheme.colors.FontColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun DeadlineTabs(
    selectedBucket: ProjectBoardViewModel.DeadlineBucket,
    onSelectBucket: (ProjectBoardViewModel.DeadlineBucket) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        DeadlineTabItem(
            text = "Hari ini",
            selected = selectedBucket == ProjectBoardViewModel.DeadlineBucket.TODAY,
            onClick = { onSelectBucket(ProjectBoardViewModel.DeadlineBucket.TODAY) }
        )
        DeadlineTabItem(
            text = "Besok",
            selected = selectedBucket == ProjectBoardViewModel.DeadlineBucket.TOMORROW,
            onClick = { onSelectBucket(ProjectBoardViewModel.DeadlineBucket.TOMORROW) }
        )
        DeadlineTabItem(
            text = "Minggu ini",
            selected = selectedBucket == ProjectBoardViewModel.DeadlineBucket.THIS_WEEK,
            onClick = { onSelectBucket(ProjectBoardViewModel.DeadlineBucket.THIS_WEEK) }
        )
    }
}

@Composable
private fun DeadlineTabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(112.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = text,
            style = if (selected) Heading_H2 else Subtitle_1,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.textMuted,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .width(if (selected) 96.dp else 0.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(if (selected) MaterialTheme.colors.primary else Color.Transparent)
        )
    }
}

@Composable
private fun ProjectHorizontalList(
    projects: List<ProjectWithTasks>,
    selectedProjectId: Long?,
    onSelectProject: (Long) -> Unit
) {
    if (projects.isEmpty()) {
        Text(
            text = "Belum ada project untuk ditampilkan.",
            style = Ket_1,
            color = MaterialTheme.colors.textMuted
        )
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        projects.forEach { projectWithTasks ->
            val project = projectWithTasks.project
            val isSelected = project.id == selectedProjectId
            val accentColor = MaterialTheme.colors.primary
            val totalTasks = projectWithTasks.tasks.size
            val completedTasks = projectWithTasks.tasks.count { it.status == TaskStatus.DONE }
            val completionProgress = if (totalTasks == 0) {
                0f
            } else {
                (completedTasks.toFloat() / totalTasks.toFloat()).coerceIn(0f, 1f)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .width(104.dp)
                        .height(104.dp)
                        .clickable { onSelectProject(project.id) },
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MaterialTheme.colors.surfaceElevated,
                    elevation = 8.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (isSelected) {
                            val infiniteTransition = rememberInfiniteTransition()
                            val wavePhase by infiniteTransition.animateFloat(
                                initialValue = 0f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(
                                        durationMillis = 3200,
                                        easing = LinearEasing
                                    ),
                                    repeatMode = RepeatMode.Restart
                                )
                            )
                            Canvas(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxSize()
                            ) {
                                val width = size.width
                                val height = size.height
                                if (completionProgress > 0f) {
                                    val waterLevel = height * (1f - completionProgress)
                                    val isFull = completionProgress >= 0.995f
                                    val waveLength = width * 1.25f
                                    val waveHeight = if (isFull) 0f else height * 0.035f
                                    val horizontalOffset = if (isFull) 0f else -wavePhase * waveLength

                                    fun wavePath(): Path {
                                        val path = Path()
                                        var startX = horizontalOffset - waveLength
                                        path.moveTo(startX, waterLevel)
                                        if (isFull) {
                                            path.lineTo(width, waterLevel)
                                        } else {
                                            while (startX < width + waveLength) {
                                                path.quadraticBezierTo(
                                                    startX + waveLength * 0.25f,
                                                    waterLevel - waveHeight,
                                                    startX + waveLength * 0.5f,
                                                    waterLevel
                                                )
                                                path.quadraticBezierTo(
                                                    startX + waveLength * 0.75f,
                                                    waterLevel + waveHeight,
                                                    startX + waveLength,
                                                    waterLevel
                                                )
                                                startX += waveLength
                                            }
                                        }
                                        path.lineTo(width, height)
                                        path.lineTo(0f, height)
                                        path.close()
                                        return path
                                    }

                                    drawPath(
                                        path = wavePath(),
                                        color = accentColor.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colors.textMuted.copy(alpha = 0.08f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                        }

                        Text(
                            text = project.name.take(1).uppercase(),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(bottom = if (isSelected) 6.dp else 0.dp),
                            style = Heading_H1,
                            color = if (isSelected) MaterialTheme.colors.FontColor else MaterialTheme.colors.textMuted
                        )
                    }
                }

                Text(
                    text = project.name,
                    style = Subtitle_2,
                    color = if (isSelected) MaterialTheme.colors.FontColor else MaterialTheme.colors.textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(104.dp)
                )
            }
        }
    }
}

@Composable
private fun ProjectTaskCard(
    task: TaskEntity,
    onEdit: () -> Unit,
    onPlay: () -> Unit,
    onToggleDone: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = MaterialTheme.colors.surfaceElevated,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.4.dp,
                        color = MaterialTheme.colors.textMuted.copy(alpha = 0.45f),
                        shape = CircleShape
                    )
                    .clickable(onClick = onToggleDone)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.title,
                        style = Subtitle_1,
                        color = MaterialTheme.colors.FontColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit task",
                        tint = MaterialTheme.colors.textMuted,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(onClick = onEdit)
                    )
                }
                Text(
                    text = "${task.estimatedMinutes} min",
                    style = Subtitle_2,
                    color = MaterialTheme.colors.textMuted
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "Play pomodoro",
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onPlay),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun CompletedTaskRow(task: TaskEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = MaterialTheme.colors.surfaceElevated,
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.72f))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = task.title,
                    style = Subtitle_2,
                    color = MaterialTheme.colors.FontColor
                )
                Text(
                    text = projectDeadlineLabel(task.dueDate),
                    style = Ket_1,
                    color = MaterialTheme.colors.textMuted
                )
            }
        }
    }
}

private fun projectDeadlineLabel(date: Date?): String {
    val dueDate = date ?: return "Tanpa deadline"
    val localDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val today = LocalDate.now()
    return when {
        localDate == today -> "Deadline hari ini"
        localDate == today.plusDays(1) -> "Deadline besok"
        else -> "Deadline ${localDate.dayOfMonth}/${localDate.monthValue}"
    }
}
