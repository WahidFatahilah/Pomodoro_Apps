package com.moa.pomodoroapps.ui.screen.project


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.ui.theme.*

@Preview
@Composable
fun ProjectUI(

) {
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundscreen)
    ) {
        topbar("Proyek Saya")
        Text(text = "Project Anda", modifier = Modifier.padding(
            start = 20.dp,
            top = 10.dp,
            end = 20.dp,
            bottom = 10.dp
        ), style = h1, color = MaterialTheme.colors.font)
        project()
        taskongoing()
        taskdone()
    }


}

@Composable
fun topbar(
    title: String,
) {
    Spacer(Modifier.width(2.dp))
   TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp){
       Row(
           modifier = Modifier.fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically
       ) {
           Text(
               text = title,
               style = h1,
               color = MaterialTheme.colors.font,
               modifier = Modifier
                   .weight(1f)
                   .wrapContentWidth(align = Alignment.CenterHorizontally)

           )
           Spacer(Modifier.width(8.dp))
           IconButton(onClick = { /* TODO */ },modifier = Modifier.align(Alignment.CenterVertically)) {
               Image(
                   painter = painterResource(id = com.moa.pomodoroapps.R.drawable.icon_delete),
                   contentDescription = "Navigation Icon"
               )
           }
       }
   }
    Divider(
        color = MaterialTheme.colors.onSurface,
        thickness = 0.5.dp,
        modifier = Modifier.padding(start = 25.dp, top = 2.dp, end = 25.dp, bottom = 5.dp)
    )

}


@Composable
fun tabBar(


){

}
@Composable
fun project() {
    val projects = listOf(
        "Ilustrasi",
        "Design",
        "Android App",
        "Website",
        "Video Editing"
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(projects) { project ->
            val title = project.first().toString().toUpperCase()

            Column(
                modifier = Modifier
                    .width(150.dp)
                    .padding(end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    // TODO: Replace with actual progress indicator
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colors.grey, RoundedCornerShape(16.dp))
                            .clickable( onClick = {

                            }
                            )
                    )
                    Text(
                        text = (title),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colors.font,
                        style = h1,
                    )

                }
                Text(
                    text = if (project.length >9 ) "${project.substring(0, 9)}..." else project,
                    style = keterangan1,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}
@Composable
fun taskongoing() {

}
@Composable
fun taskdone() {

}

@Composable
fun MyTabBar() {
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        TabRow(selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> { /* content for tab 1 */ }
            1 -> { /* content for tab 2 */ }
            2 -> { /* content for tab 3 */ }
        }
    }
}