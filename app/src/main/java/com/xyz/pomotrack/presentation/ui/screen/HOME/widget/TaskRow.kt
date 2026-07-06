package com.xyz.pomotrack.presentation.ui.screen.HOME.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xyz.pomotrack.Data.Task
import com.xyz.pomotrack.Data.TaskStatus
import com.xyz.pomotrack.Data.TaskWithProjectRow
import com.xyz.pomotrack.R
import com.xyz.pomotrack.presentation.ui.theme.FontColor
import com.xyz.pomotrack.presentation.ui.theme.Ket_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_2
import com.xyz.pomotrack.presentation.ui.theme.focusAccent
import com.xyz.pomotrack.presentation.ui.theme.surfaceElevated
import com.xyz.pomotrack.presentation.ui.theme.surfaceMuted
import com.xyz.pomotrack.presentation.ui.theme.textMuted
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun TaskRow(
    task: Task,
    onClickRow: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
    onClickDone: (Task) -> Unit,
    onClickPlayPomo: (Task) -> Unit
) {
    val taskAlpha = if (task.isDone) 0.62f else 1f
    val titleStyle = if (task.isDone) {
        Subtitle_1.copy(textDecoration = TextDecoration.LineThrough)
    } else {
        Subtitle_1
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClickRow(task) }
            .alpha(taskAlpha),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MaterialTheme.colors.surfaceElevated,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onClickDone(task) }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.focusAccent)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.project,
                        style = Ket_1,
                        color = MaterialTheme.colors.textMuted
                    )
                }

                Text(
                    text = task.title,
                    style = titleStyle,
                    color = MaterialTheme.colors.FontColor
                )

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = if (task.isDone) TextStyle(textDecoration = TextDecoration.LineThrough) else Subtitle_2,
                        color = MaterialTheme.colors.textMuted,
                        maxLines = 2
                    )
                }

                TaskMetaRow(deadlineText = task.deadline.toDeadlineText())
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { if (!task.isDone) onClickPlayPomo(task) },
                    enabled = !task.isDone,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.surfaceMuted)
                ) {
                    Icon(
                        painter = painterResource(id = if (task.isDone) R.drawable.ic_dont_play else R.drawable.ic_play),
                        contentDescription = "Play Pomodoro",
                        tint = MaterialTheme.colors.focusAccent
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                IconButton(onClick = { onClickDelete(task) }, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.deletelogo),
                        contentDescription = "Delete Task",
                        tint = MaterialTheme.colors.textMuted
                    )
                }
            }
        }
    }
}

@Composable
fun TaskRow(
    task: TaskWithProjectRow,
    onClickRow: (TaskWithProjectRow) -> Unit,
    onClickDelete: (TaskWithProjectRow) -> Unit,
    onClickDone: (TaskWithProjectRow) -> Unit,
    onClickPlayPomo: (TaskWithProjectRow) -> Unit
) {
    val isDone = task.status == TaskStatus.DONE
    val taskAlpha = if (isDone) 0.62f else 1f
    val titleStyle = if (isDone) {
        Subtitle_1.copy(textDecoration = TextDecoration.LineThrough)
    } else {
        Subtitle_1
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClickRow(task) }
            .alpha(taskAlpha),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MaterialTheme.colors.surfaceElevated,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDone,
                onCheckedChange = { onClickDone(task) }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.focusAccent)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.projectName,
                        style = Ket_1,
                        color = MaterialTheme.colors.textMuted
                    )
                }

                Text(
                    text = task.title,
                    style = titleStyle,
                    color = MaterialTheme.colors.FontColor
                )

                if (!task.description.isNullOrBlank()) {
                    Text(
                        text = task.description,
                        style = if (isDone) TextStyle(textDecoration = TextDecoration.LineThrough) else Subtitle_2,
                        color = MaterialTheme.colors.textMuted,
                        maxLines = 2
                    )
                }

                TaskMetaRow(deadlineText = task.dueDate.toDeadlineText())
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { if (!isDone) onClickPlayPomo(task) },
                    enabled = !isDone,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.surfaceMuted)
                ) {
                    Icon(
                        painter = painterResource(id = if (isDone) R.drawable.ic_dont_play else R.drawable.ic_play),
                        contentDescription = "Play Pomodoro",
                        tint = MaterialTheme.colors.focusAccent
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                IconButton(onClick = { onClickDelete(task) }, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.deletelogo),
                        contentDescription = "Delete Task",
                        tint = MaterialTheme.colors.textMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskMetaRow(deadlineText: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_date_range_24),
            contentDescription = "Deadline",
            tint = MaterialTheme.colors.textMuted,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = deadlineText, style = Ket_1, color = MaterialTheme.colors.textMuted)
    }
}

private fun Date?.toDeadlineText(): String {
    val deadline = this ?: return "No deadline"
    val deadlineDate = deadline.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    if (deadlineDate == LocalDate.now()) return "Today"
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(deadline)
}

@Preview
@Composable
fun TaskRowPreview() {
    TaskRow(
        task = Task(
            project = "Project Name",
            title = "Task testing",
            description = "Prepare outline and run the first focus session",
            deadline = Date(),
            isDone = false
        ),
        onClickRow = {},
        onClickDelete = {},
        onClickDone = {},
        onClickPlayPomo = {}
    )
}
