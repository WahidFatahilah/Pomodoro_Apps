package com.moa.pomodoroapps.todo.components

import android.graphics.Paint.Style
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Pink
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_1
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Composable
fun TaskRow(
    task: Task,
    onClickRow: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
    onClickDone: (Task) -> Unit,
    onClickPlayPomo: (Task) -> Unit
) {

   var checkedState = remember { mutableStateOf(task.isDone) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClickRow(task) },
        elevation = 5.dp
    ) {
        Column(
            Modifier.padding(10.dp)
        ) {
            Text(text = task.project, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough,) else Subtitle_1)

            Row(
                modifier = Modifier
                    .clickable { onClickRow(task) }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 3.dp, height = 24.dp)
                        .background(MaterialTheme.colors.Pink)
                )
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Row() {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable .ic_task) ,
                            contentDescription = "Project",
                            tint = MaterialTheme.colors.FontColor,
                        )
                        Text(text = task.title, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                    }
                    Row(){
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable .baseline_description_24) ,
                            contentDescription = "Project",
                            tint = MaterialTheme.colors.FontColor,
                        )
                        Text(text = task.description, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                    }
                    Row(){
                        val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        val deadlineString = task.deadline?.let {
                            val deadlineDate = it.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate()
                            if (deadlineDate == LocalDate.now()) {
                                "Hari ini"
                            } else {
                                dateFormatter.format(it)
                            }
                        } ?: "No deadline"
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable .baseline_date_range_24) ,
                            contentDescription = "Project",
                            tint = MaterialTheme.colors.FontColor,
                        )
                        Text(text = deadlineString, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Checkbox(
                    checked =  checkedState.value,
                    onCheckedChange = { isChecked ->
                        checkedState.value = isChecked
                        onClickDone(task)
                    }
                )

                IconButton(onClick = { onClickDelete(task) }) {
                    Icon(painter = painterResource(id = R.drawable.deletelogo), contentDescription = "Delete TAsk")
                }
                if (checkedState.value == true)
                {
                    IconButton(onClick = {  }) {
                        Icon(painter = painterResource(id = R.drawable.ic_dont_play), contentDescription = "Play Pomodoro")
                    }
                }else {
                    IconButton(onClick = { onClickPlayPomo(task) }) {
                        Icon(painter = painterResource(id = R.drawable.ic_play), contentDescription = "Play Pomodoro")
                    }
                }

            }
        }

    }
}

@Composable
fun CircleCheckbox(
    task: Task,
    checked: Boolean,
    onClickDone: (Task) -> Unit
) {
    val strokeWidth = 2.dp
    val size = 24.dp

    Box(
        modifier = Modifier
            .size(size)
            .clickable(onClick = { onClickDone(task) })
            .border(
                width = strokeWidth,
                color = MaterialTheme.colors.onBackground,
                shape = CircleShape
            )
    ) {
        if (checked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "Checkbox Checked",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(strokeWidth)
            )
        }
    }
}

@Preview
@Composable
fun TaskRowPreview() {
    val dateFormat = SimpleDateFormat("MM/dd/yy")
    val date = dateFormat.parse("12/22/22")

    TaskRow(
        task = Task(project = "Project Name", title = "TaskTesting", description = "LoremIpsum", deadline = date, isDone = false ),
        onClickRow ={},
        onClickDelete ={},
        onClickDone = {},
        onClickPlayPomo = {}
    )
}