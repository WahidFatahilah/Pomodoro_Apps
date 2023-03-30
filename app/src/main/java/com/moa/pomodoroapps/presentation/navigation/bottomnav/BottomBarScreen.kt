package com.moa.pomodoroapps.presentation.navigation.bottomnav

import com.moa.pomodoroapps.R


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
){
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = R.drawable.homebottombar
    )
    object Pomodoro: BottomBarScreen(
        route = "pomodoro",
        title = "Pomodoro",
        icon = R.drawable.timerlogo
    )
    object Statistik: BottomBarScreen(
        route = "statistik",
        title = "Statistik",
        icon = R.drawable.statistiklogo
    )
}
