package com.moa.pomodoroapps

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.presentation.navigation.bottomnav.BottomBar
import com.moa.pomodoroapps.presentation.navigation.bottomnav.BottomNavGraph
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor


@ExperimentalPagerApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    var isDarkTheme by remember { mutableStateOf(false) }

    val systemTheme = isSystemInDarkTheme()
    if (systemTheme != isDarkTheme) {
        isDarkTheme = systemTheme
    }

    val colors = if (isDarkTheme) {
        darkColors()
    } else {
        lightColors()
    }

    val navController = rememberNavController()


        Scaffold(
            backgroundColor = MaterialTheme.colors.backgroundColor,
            // topBar = { CustomTopAppBar() },

            bottomBar = { BottomBar(navController = navController) },

            // modifier = Modifier.padding(top = 40.dp)
        ) {
            // SCAFFOLD KONTEN
            BottomNavGraph(navController = navController)
        }




}
