package com.moa.pomodoroapps.ui.feature.appbarcustom.bottomnav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moa.pomodoroapps.screen.HomeScreen
import com.moa.pomodoroapps.screen.ProjectScreen
import com.moa.pomodoroapps.screen.StatistikScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ){
        composable(route = BottomBarScreen.Home.route){
            HomeScreen()
        }
        composable(route = BottomBarScreen.Pomodoro.route){
            ProjectScreen()
        }
        composable(route = BottomBarScreen.Statistik.route){
            StatistikScreen()
        }
    }
}
