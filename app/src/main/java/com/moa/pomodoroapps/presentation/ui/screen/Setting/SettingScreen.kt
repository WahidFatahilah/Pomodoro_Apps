package com.moa.pomodoroapps.presentation.ui.screen.Setting

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Ket_1

@Composable
fun SettingScreen( viewModel : SettingScreenViewModel = hiltViewModel(), navController: NavController) {

    val isDarkTheme = ThemePreferences(LocalContext.current).isDarkTheme()


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
                    checked = isDarkTheme,
                    onCheckedChange = { }
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
        }

    }



}

@Preview
@Composable
fun PreviewStatistikScreen() {
    //SettingScreen(viewModel = SettingScreenViewModel())
}

class ThemePreferences(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("THEME_PREFS", Context.MODE_PRIVATE)

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean("IS_DARK_THEME", false)
    }

    fun setDarkTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean("IS_DARK_THEME", isDarkTheme).apply()
    }


}
