package com.moa.pomodoroapps.presentation.navigation.bottomnav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.presentation.ui.screen.Project.ProjectScreen
import com.moa.pomodoroapps.presentation.ui.screen.HOME.HomeContent
import com.moa.pomodoroapps.presentation.ui.screen.Setting.SettingScreenViewModel
import com.moa.pomodoroapps.presentation.ui.screen.SettingScreen

@ExperimentalPagerApi
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ){
        composable(route = BottomBarScreen.Home.route){
            HomeContent(navController = navController)
        }

        composable(route = BottomBarScreen.Pomodoro.route){
            ProjectScreen()
        }

        composable(route = BottomBarScreen.Setting.route){
            SettingScreen(navController = navController)
        }

    }
}