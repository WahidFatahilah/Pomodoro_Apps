package com.xyz.pomotrack.presentation.ui.screen.Setting

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xyz.pomotrack.R
import com.xyz.pomotrack.dataStore
import com.xyz.pomotrack.presentation.ui.theme.FontColor
import com.xyz.pomotrack.presentation.ui.theme.Heading_H1
import com.xyz.pomotrack.presentation.ui.theme.Ket_1
import com.xyz.pomotrack.presentation.ui.theme.backgroundColor
import com.xyz.pomotrack.presentation.ui.theme.textMuted

@Composable
fun SettingScreen(
    viewModel: SettingScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val viewModelTheme = remember { ThemeViewModel(context.dataStore) }
    val value = viewModelTheme.state.observeAsState().value
    val systemInDarkTheme = isSystemInDarkTheme()

    val darkModeChecked by remember(value) {
        mutableStateOf(value ?: systemInDarkTheme)
    }

    LaunchedEffect(viewModel) {
        viewModelTheme.request()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = "Settings",
                style = Heading_H1,
                color = MaterialTheme.colors.FontColor
            )
            Text(
                text = "Tune the app to match your focus style.",
                style = Ket_1,
                color = MaterialTheme.colors.textMuted
            )
        }

        SettingsCard(title = "Appearance") {
            SettingsSwitchItem(
                icon = R.drawable.baseline_settings_24,
                title = "Dark Mode",
                subtitle = if (darkModeChecked) "Using dark theme" else "Using light theme",
                checked = darkModeChecked,
                onCheckedChange = { viewModelTheme.switchToUseDarkMode(it) }
            )
        }

        SettingsCard(title = "Preferences") {
            SettingsActionItem(
                icon = R.drawable.baseline_description_24,
                title = "Language",
                subtitle = "English and Indonesian content",
                onClick = { }
            )
            SettingsActionItem(
                icon = R.drawable.ic_task,
                title = "Default Focus",
                subtitle = "Pomodoro, short break, and long break timing",
                onClick = { navController.navigate("testScreen") }
            )
        }

        SettingsCard(title = "Support") {
            SettingsActionItem(
                icon = R.drawable.baseline_description_24,
                title = "Privacy Policy",
                subtitle = "Read how app data is handled",
                onClick = { }
            )
            SettingsActionItem(
                icon = R.drawable.ic_done,
                title = "Rate App",
                subtitle = "Share feedback for the Pomodoro app",
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(76.dp))
    }
}
