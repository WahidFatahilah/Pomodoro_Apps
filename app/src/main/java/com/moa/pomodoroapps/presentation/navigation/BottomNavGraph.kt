package com.moa.pomodoroapps.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.MainScreen
import com.moa.pomodoroapps.OpenOnboardingScreen
import com.moa.pomodoroapps.presentation.ui.screen.Project.ProjectScreen
import com.moa.pomodoroapps.presentation.ui.screen.HOME.HomeContent
import com.moa.pomodoroapps.presentation.ui.screen.IntroScreen.IntroScreen
import com.moa.pomodoroapps.presentation.ui.screen.Pomodoro.PomodoroScreen
import com.moa.pomodoroapps.presentation.ui.screen.Setting.SettingScreen
import com.moa.pomodoroapps.presentation.ui.screen.Setting.TestScreen

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
            ProjectScreen()
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
        composable("openOnboardingScreen") {
            OpenOnboardingScreen()
        }

        composable("settingScreen") {
            SettingScreen(navController = navController)
        }
        composable("testScreen") {
            TestScreen(navController = navController)
        }

        composable(
            "pomodoro/{taskName}",
            arguments = listOf(navArgument("taskName") { defaultValue = "" }),
        ) { backStackEntry ->
            var taskName = backStackEntry.arguments?.getString("taskName") ?: ""
            PomodoroScreen(onBackPressed = {
                taskName = ""
                navController.popBackStack()
            },  taskName = taskName, navController = navController)

        }


    }
}