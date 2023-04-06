package com.moa.pomodoroapps.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.screen.Setting.SettingScreenViewModel

@Composable
fun SettingScreen( viewModel : SettingScreenViewModel = hiltViewModel(), navController: NavController) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(text = "Ubah Tema")
                Text(text = "Tema Sekarang")
            }
            Switch(checked = true, onCheckedChange = {})
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row() {
                Text(text = "Bahasa")
                //Image(painter = , contentDescription = )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(contentDescription = "", painter = painterResource(id = R.drawable.ic_arrow), modifier = Modifier.clickable {  })
            }
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row() {
                Text(text = "Kebijakan Privasi")
                //Image(painter = , contentDescription = )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(contentDescription = "", painter = painterResource(id = R.drawable.ic_arrow), modifier = Modifier.clickable {  })
            }
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row() {
                Text(text = "Rating Aplikasi")
                //Image(painter = , contentDescription = )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(contentDescription = "", painter = painterResource(id = R.drawable.ic_arrow), modifier = Modifier.clickable {  })
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.2f)) {

        }
    }

}

@Preview
@Composable
fun PreviewStatistikScreen() {
    //SettingScreen(viewModel = SettingScreenViewModel())
}