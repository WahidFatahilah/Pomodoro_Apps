package com.moa.pomodoroapps

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.presentation.ui.screen.IntroScreen.IntroScreen
import com.moa.pomodoroapps.presentation.ui.screen.SplashScreen
import com.moa.pomodoroapps.presentation.ui.theme.AppTheme
import com.moa.pomodoroapps.presentation.ui.theme.PomodoroAppsTheme
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val Context.dataStore by preferencesDataStore("settings")

@AndroidEntryPoint
@ExperimentalPagerApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.backgroundColor
                )
                {
                    var isShowSplash by remember { mutableStateOf(true) }

                    LaunchedEffect(isShowSplash) {
                        if (isShowSplash) {
                            delay(1500) // Wait for 3 seconds
                            isShowSplash = false // Navigate to MainScreen
                        }
                    }

                    if (isShowSplash) {
                        SplashScreen()
                    } else {
                        MainScreen()
                    }

                }
            }
        }
    }
}

