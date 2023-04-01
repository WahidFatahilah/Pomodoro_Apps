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
import com.moa.pomodoroapps.Data.Project
import java.text.SimpleDateFormat

@Composable
fun ProjectRow(
    Project: Project,
    onClickRow: (Project) -> Unit,
    onClickDelete: (Project) -> Unit,
    onClickDone: (Project) -> Unit,
    onClickPlayPomo: (Project) -> Unit
) {

   var checkedState = remember { mutableStateOf(Project.isDone) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClickRow(Project) },
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .clickable { onClickRow(Project) }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(text = Project.project, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = Project.title, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = Project.description, style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
                Text(text = Project.deadline?.toString() ?: "No Deadline", style = if (checkedState.value) TextStyle(textDecoration = TextDecoration.LineThrough) else LocalTextStyle.current)
            }
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked =  checkedState.value,
                onCheckedChange = { isChecked ->
                    checkedState.value = isChecked
                    onClickDone(Project)
                }
            )
            IconButton(onClick = { onClickDelete(Project) }) {
                Icon(painter = painterResource(id = R.drawable.deletelogo), contentDescription = "Delete Project")
            }
            IconButton(onClick = { onClickPlayPomo(Project) }) {
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

    ProjectRow(
        Project = Project(project = "Project Name", title = "ProjectTesting", description = "LoremIpsum", deadline = date, isDone = false ),
        onClickRow ={},
        onClickDelete ={},
        onClickDone = {},
        onClickPlayPomo = {}
    )
}