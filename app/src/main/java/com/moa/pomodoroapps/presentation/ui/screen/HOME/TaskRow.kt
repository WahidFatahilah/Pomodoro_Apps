package com.moa.pomodoroapps.todo.components

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
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.Data.Task
import java.text.SimpleDateFormat

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
        Row(
            modifier = Modifier
                .clickable { onClickRow(task) }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(text = task.project, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = task.title, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = task.description, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = task.deadline?.toString() ?: "No Deadline", style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
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
            IconButton(onClick = { onClickPlayPomo(task) }) {
                Icon(painter = painterResource(id = R.drawable.ic_play), contentDescription = "Play Pomodoro")
            }
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