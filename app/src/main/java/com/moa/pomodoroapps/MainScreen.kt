package com.moa.pomodoroapps

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.presentation.navigation.BottomBar
import com.moa.pomodoroapps.presentation.navigation.NavigationGraph
import com.moa.pomodoroapps.presentation.ui.screen.IntroScreen.IntroScreen
import com.moa.pomodoroapps.presentation.ui.screen.Project.loadLottie
import com.moa.pomodoroapps.presentation.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@ExperimentalPagerApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = remember { OnboardingDataStore(context) }

    val onboardingCompleted = dataStore.onboardingCompleted
        .collectAsState(initial = false)



    LaunchedEffect(onboardingCompleted.value) {
        if (!onboardingCompleted.value) {
            navController.navigate("introScreen")
        }
    }
    LaunchedEffect(key1 = true) {
        delay(3000L)
    }


    Scaffold(
        backgroundColor = MaterialTheme.colors.backgroundColor,
        bottomBar = { BottomBar(navController = navController) },
    )
    {
        NavigationGraph(navController = navController)
    }
}

object PreferencesKeys {
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
}

class OnboardingDataStore(context: Context) {
    private val dataStore = context.applicationContext.dataStore

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
    }

}