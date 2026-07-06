package com.xyz.pomotrack.presentation.ui.screen.HOME.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.xyz.pomotrack.Data.Task
import com.xyz.pomotrack.Data.TaskStatus
import com.xyz.pomotrack.Data.TaskWithProjectRow
import com.xyz.pomotrack.R
import com.xyz.pomotrack.presentation.ui.theme.FontColor
import com.xyz.pomotrack.presentation.ui.theme.Heading_H2
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_2
import com.xyz.pomotrack.presentation.ui.theme.focusAccent
import com.xyz.pomotrack.presentation.ui.theme.surfaceElevated

@Composable
fun TaskList(
    tasks: List<Task>,
    onClickRow: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
    onClickDone: (Task) -> Unit,
    onClickPlayPomo: (Task) -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(bottom = 92.dp)){
        items(items = tasks, key = { it.id }){ task ->
            TaskRow(task = task, onClickRow = onClickRow, onClickDelete = onClickDelete, onClickPlayPomo = onClickPlayPomo, onClickDone = onClickDone)
        }
    }
}

@Composable
fun TaskListWithProject(
    tasks: List<TaskWithProjectRow>,
    onClickRow: (TaskWithProjectRow) -> Unit,
    onClickDone: (TaskWithProjectRow) -> Unit,
    onClickPlayPomo: (TaskWithProjectRow) -> Unit,
) {
    val expandedProjects = remember { mutableStateMapOf<Long, Boolean>() }
    val groupedProjects = tasks
        .groupBy { it.projectId }
        .map { (_, projectTasks) -> projectTasks.first() to projectTasks }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 148.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        groupedProjects.forEach { (project, projectTasks) ->
            val expanded = expandedProjects[project.projectId] ?: true
            ProjectTaskGroupCard(
                projectName = project.projectName,
                expanded = expanded,
                tasks = projectTasks,
                onToggleExpanded = {
                    expandedProjects[project.projectId] = !expanded
                },
                onClickRow = onClickRow,
                onClickPlayPomo = onClickPlayPomo,
                onClickDone = onClickDone
            )
        }
    }
}

@Composable
private fun ProjectTaskGroupCard(
    projectName: String,
    expanded: Boolean,
    tasks: List<TaskWithProjectRow>,
    onToggleExpanded: () -> Unit,
    onClickRow: (TaskWithProjectRow) -> Unit,
    onClickPlayPomo: (TaskWithProjectRow) -> Unit,
    onClickDone: (TaskWithProjectRow) -> Unit
) {
    val completedCount = tasks.count { it.status == TaskStatus.DONE }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surfaceElevated,
        elevation = 3.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpanded)
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = projectName,
                    modifier = Modifier.weight(1f),
                    style = Subtitle_1,
                    color = MaterialTheme.colors.FontColor
                )
                Text(
                    text = "$completedCount/${tasks.size}",
                    style = Subtitle_2,
                    color = Color(0xFFC8C8C8)
                )
                Spacer(modifier = Modifier.size(12.dp))
                ProjectListChevron(expanded = expanded, modifier = Modifier.size(18.dp))
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F8F8))
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    tasks.forEach { task ->
                        SimpleProjectTaskRow(
                            task = task,
                            onClickRow = onClickRow,
                            onClickDone = onClickDone,
                            onClickPlayPomo = onClickPlayPomo
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SimpleProjectTaskRow(
    task: TaskWithProjectRow,
    onClickRow: (TaskWithProjectRow) -> Unit,
    onClickDone: (TaskWithProjectRow) -> Unit,
    onClickPlayPomo: (TaskWithProjectRow) -> Unit
) {
    val isDone = task.status == TaskStatus.DONE

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClickRow(task) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MiniTaskCheckbox(
            checked = isDone,
            onClick = { onClickDone(task) }
        )

        Spacer(modifier = Modifier.size(8.dp))

        Box(
            modifier = Modifier
                .size(width = 2.dp, height = 30.dp)
                .alpha(if (isDone) 0.45f else 1f)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colors.focusAccent)
        )

        Spacer(modifier = Modifier.size(9.dp))

        Text(
            text = task.title,
            modifier = Modifier
                .weight(1f)
                .alpha(if (isDone) 0.55f else 1f),
            style = Subtitle_1.copy(
                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
            ),
            color = MaterialTheme.colors.FontColor
        )

        IconButton(
            onClick = { if (!isDone) onClickPlayPomo(task) },
            enabled = !isDone,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isDone) R.drawable.ic_dont_play else R.drawable.ic_play),
                contentDescription = "Mulai Pomodoro",
                tint = MaterialTheme.colors.focusAccent,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun MiniTaskCheckbox(
    checked: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(7.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(17.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(if (checked) MaterialTheme.colors.focusAccent else Color.Transparent)
                .border(
                    width = 1.4.dp,
                    color = if (checked) MaterialTheme.colors.focusAccent else Color(0xFF777777),
                    shape = RoundedCornerShape(3.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Canvas(modifier = Modifier.size(10.dp)) {
                    val stroke = 1.7.dp.toPx()
                    drawLine(
                        color = Color.White,
                        start = androidx.compose.ui.geometry.Offset(size.width * 0.16f, size.height * 0.52f),
                        end = androidx.compose.ui.geometry.Offset(size.width * 0.42f, size.height * 0.78f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color.White,
                        start = androidx.compose.ui.geometry.Offset(size.width * 0.42f, size.height * 0.78f),
                        end = androidx.compose.ui.geometry.Offset(size.width * 0.86f, size.height * 0.22f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectListChevron(expanded: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.4.dp.toPx()
        val yStart = if (expanded) size.height * 0.64f else size.height * 0.36f
        val yEnd = if (expanded) size.height * 0.34f else size.height * 0.64f
        drawLine(
            color = Color(0xFF252525),
            start = androidx.compose.ui.geometry.Offset(size.width * 0.22f, yStart),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.5f, yEnd),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF252525),
            start = androidx.compose.ui.geometry.Offset(size.width * 0.78f, yStart),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.5f, yEnd),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}
