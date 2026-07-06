package com.moa.pomodoroapps.presentation.navigation

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
        title = "Project",
        icon = R.drawable.ic_task
    )
    object Statistik: BottomBarScreen(
        route = "statistik",
        title = "Statistik",
        icon = R.drawable.statistiklogo
    )
    object Setting: BottomBarScreen(
        route = "setting",
        title = "Settings",
        icon = R.drawable.baseline_settings_24
    )
}
