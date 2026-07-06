package com.xyz.pomotrack.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.xyz.pomotrack.MainScreen
import com.xyz.pomotrack.presentation.ui.screen.Project.AddTaskScreen
import com.xyz.pomotrack.presentation.ui.screen.Project.ProjectScreen
import com.xyz.pomotrack.presentation.ui.screen.HOME.HomeContent
import com.xyz.pomotrack.presentation.ui.screen.IntroScreen.IntroScreen
import com.xyz.pomotrack.presentation.ui.screen.Pomodoro.PomodoroScreen
import com.xyz.pomotrack.presentation.ui.screen.Setting.SettingScreen
import com.xyz.pomotrack.presentation.ui.screen.Setting.TestScreen
import com.xyz.pomotrack.presentation.ui.screen.Statistic.StatisticScreen

@ExperimentalPagerApi
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ){
        composable(route = BottomBarScreen.Home.route){
            HomeContent(navController = navController)
        }

        composable(route = BottomBarScreen.Pomodoro.route){
            ProjectScreen(navController = navController)
        }

        composable(route = BottomBarScreen.Statistik.route){
            StatisticScreen()
        }

        composable("addTask") {
            AddTaskScreen(navController = navController)
        }

        composable(route = BottomBarScreen.Setting.route){
            SettingScreen(navController = navController)
        }
        composable("mainScreen") {
            MainScreen()
        }
        composable("splashScreen") {
            SettingScreen(navController = navController)
        }
       composable("introScreen") {
            IntroScreen(navController = navController,modifier = Modifier.background(Color.White))
        }
        composable("settingScreen") {
            SettingScreen(navController = navController)
        }
        composable("testScreen") {
            TestScreen(navController = navController)
        }

        composable(
            "pomodoro/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            PomodoroScreen(onBackPressed = {
                navController.popBackStack()
            }, taskId = taskId)

        }


    }
}
