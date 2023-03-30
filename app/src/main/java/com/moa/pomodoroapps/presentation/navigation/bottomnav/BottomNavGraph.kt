package com.moa.pomodoroapps.presentation.navigation.bottomnav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.presentation.ui.screen.PomodoroScreen
import com.moa.pomodoroapps.presentation.ui.screen.Project.ProjectScreen
import com.moa.pomodoroapps.presentation.ui.screen.StatistikScreen
import com.moa.pomodoroapps.todo.data.HomeContent
import com.moa.pomodoroapps.todo.data.PomodoroTimer

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


        composable(route = BottomBarScreen.Statistik.route){
            StatistikScreen()
        }
    }
}
