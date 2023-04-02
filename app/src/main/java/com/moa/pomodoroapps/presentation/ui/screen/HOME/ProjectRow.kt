package com.moa.pomodoroapps.todo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.Data.Project
import com.moa.pomodoroapps.Data.Task
import com.moa.pomodoroapps.presentation.ui.screen.HOME.TaskRow
import java.text.SimpleDateFormat

@Composable
fun ProjectRow(
    project: Project,
    onClickRow: (Project) -> Unit,
    onClickDelete: (Project) -> Unit,
    onClickDone: (Project) -> Unit,
    onClickPlayPomo: (Project) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClickRow(project) },
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(16.dp)
                .clickable { onClickRow(project) }
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tasks",
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(project.tasks.size) { index ->
                    TaskRow(
                        Task = project.tasks[index],
                        onClickTaskRow = {},
                        onClickTaskDelete = {},
                        onClickTaskDone = {},
                        onClickTaskPlayPomo = {}
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onClickDelete(project) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.deletelogo),
                        contentDescription = "Delete Project"
                    )
                }
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
        project = Project(name = "ss", ),
        onClickRow ={},
        onClickDelete ={},
        onClickDone = {},
        onClickPlayPomo = {}
    )
}