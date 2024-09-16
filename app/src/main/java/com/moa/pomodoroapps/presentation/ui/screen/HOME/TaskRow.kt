package com.moa.pomodoroapps.presentation.ui.screen.HOME

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.Data.Project
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.R
import java.text.SimpleDateFormat

@Composable
fun TaskRow(
    Task: Task,
    onClickTaskRow: (Task) -> Unit,
    onClickTaskDelete: (Task) -> Unit,
    onClickTaskDone: (Task) -> Unit,
    onClickTaskPlayPomo: (Task) -> Unit
) {

    var checkedState = remember { mutableStateOf(Task.isDone) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClickTaskRow(Task) },
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { onClickTaskRow(Task) }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(text = Task.name, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = Task.description, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = Task.deadline?.toString() ?: "No Deadline", style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
            }
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked =  checkedState.value,
                onCheckedChange = { isChecked ->
                    checkedState.value = isChecked
                    onClickTaskDone(Task)
                }
            )
            IconButton(onClick = { onClickTaskDelete(Task) }) {
                Icon(painter = painterResource(id = R.drawable.deletelogo), contentDescription = "Delete Project")
            }
            IconButton(onClick = { onClickTaskPlayPomo(Task) }) {
                Icon(painter = painterResource(id = R.drawable.ic_play), contentDescription = "Play Pomodoro")
            }
        }
    }
}

@Preview
@Composable
fun ProjectRowPreview() {
    val dateFormat = SimpleDateFormat("MM/dd/yy")
    val date = dateFormat.parse("12/22/22")

    TaskRow(
        Task = Task(name = "ProjectTesting", description = "LoremIpsum", deadline = date, isDone = false , projectId = 0),
        onClickTaskRow ={},
        onClickTaskDelete ={},
        onClickTaskDone = {},
        onClickTaskPlayPomo = {}
    )
}