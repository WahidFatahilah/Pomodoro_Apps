package com.moa.pomodoroapps

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.presentation.ui.screen.IntroScreen.IntroScreen
import com.moa.pomodoroapps.presentation.ui.theme.AppTheme
import com.moa.pomodoroapps.presentation.ui.theme.PomodoroAppsTheme
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor
import dagger.hilt.android.AndroidEntryPoint


val Context.dataStore by preferencesDataStore("settings")

@AndroidEntryPoint
@ExperimentalPagerApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme() {
               // val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.backgroundColor
                ) {
                    MainScreen()
                   // OpenOnboardingScreen()
                }
            }
        }
    }
}

