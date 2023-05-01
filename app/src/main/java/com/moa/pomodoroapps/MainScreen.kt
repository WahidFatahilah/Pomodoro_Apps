package com.moa.pomodoroapps

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.presentation.navigation.BottomBar
import com.moa.pomodoroapps.presentation.navigation.NavigationGraph
import com.moa.pomodoroapps.presentation.ui.screen.IntroScreen.IntroScreen
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor


@ExperimentalPagerApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        backgroundColor = MaterialTheme.colors.backgroundColor,
        // topBar = { CustomTopAppBar() },

        bottomBar = { BottomBar(navController = navController) },

        // modifier = Modifier.padding(top = 40.dp)
    )
    {
        NavigationGraph(navController = navController)
    }


}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OpenOnboardingScreen() {
    val navController = rememberNavController()
    Scaffold(
    )
    {
        IntroScreen(navController = navController, modifier = Modifier.background(Color.White))
    }
}