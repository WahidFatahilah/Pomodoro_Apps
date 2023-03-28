package com.moa.pomodoroapps.todo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.todo.data.Task

@Composable
fun TaskRow(
    task: Task,
    onClickRow: (Task) -> Unit,
    onClickDelete: (Task) -> Unit
) {
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
            Text(text = task.title)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onClickDelete(task) }) {
                Icon(painter = painterResource(id = R.drawable.deletelogo), contentDescription = "Delete TAsk")
            }
        }
    }
}

@Preview
@Composable
fun TaskRowPreview() {
    TaskRow(
        task = Task(title = "TaskTesting", description = "LoremIpsum"),
        onClickRow ={},
        onClickDelete ={}
    )
}