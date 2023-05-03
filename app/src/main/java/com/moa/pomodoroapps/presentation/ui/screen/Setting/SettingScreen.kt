package com.moa.pomodoroapps.presentation.ui.screen.Setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.dataStore
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Ket_1

@Composable
fun SettingScreen( viewModel : SettingScreenViewModel = hiltViewModel(), navController: NavController) {
    val context = LocalContext.current


    val viewModelTheme = remember { ThemeViewModel(context.dataStore)  }
    val value = viewModelTheme.state.observeAsState().value
    val systemInDarkTheme = isSystemInDarkTheme()


    val darkModeChecked by remember(value) {
        val checked = when (value) {
            null -> systemInDarkTheme
            else -> value
        }
        mutableStateOf(checked)
    }
    val useDeviceModeChecked by remember(value) {
        val checked = when (value) {
            null -> true
            else -> false
        }
        mutableStateOf(checked)
    }

    LaunchedEffect(viewModel) {
        viewModelTheme.request()
    }




    MaterialTheme(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .padding(15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
                    .clickable { }
                    .fillMaxHeight(0.2f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Text(text = "Ubah Tema", style = Heading_H2, color = MaterialTheme.colors.FontColor)
                    Text(text = "Tema Sekarang", style = Ket_1, color = MaterialTheme.colors.FontColor)
                }
                Switch(
                    checked = darkModeChecked,
                    onCheckedChange = { viewModelTheme.switchToUseDarkMode(it) }
                )
            }
            Divider(modifier = Modifier.background(MaterialTheme.colors.FontColor))
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
                    .clickable { }
                    .fillMaxHeight(0.2f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row() {
                    Text(text = "Bahasa", style = Heading_H2, color = MaterialTheme.colors.FontColor)
                    //Image(painter = , contentDescription = )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(contentDescription = "", painter = painterResource(id = R.drawable.ic_arrow), modifier = Modifier.clickable {  })
                }
            }
            Divider(modifier = Modifier.background(MaterialTheme.colors.FontColor))
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
                    .clickable { }
                    .padding(10.dp)
                    .fillMaxHeight(0.2f),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row() {
                    Text(text = "Kebijakan Privasi", style = Heading_H2, color = MaterialTheme.colors.FontColor)
                    //Image(painter = , contentDescription = )
                }
                IconButton(onClick = {   /*val uri = Uri.parse(link)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent) */}) {
                    Icon(contentDescription = "", painter = painterResource(id = R.drawable.ic_arrow), modifier = Modifier.clickable {  })
                }
            }
            Divider(modifier = Modifier.background(MaterialTheme.colors.FontColor))
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
                    .clickable { }
                    .fillMaxHeight(0.2f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row() {
                    Text(text = "Rating Aplikasi", style = Heading_H2, color = MaterialTheme.colors.FontColor)
                    //Image(painter = , contentDescription = )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(contentDescription = "", painter = painterResource(id = R.drawable.ic_arrow), modifier = Modifier.clickable {  })
                }
            }
            Divider(modifier = Modifier.background(MaterialTheme.colors.FontColor))

            Button(onClick = { navController.navigate("TestScreen") }) {

            }
        }
    }
}