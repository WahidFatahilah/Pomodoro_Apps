package com.moa.pomodoroapps.presentation.ui.theme


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moa.pomodoroapps.dataStore
import com.moa.pomodoroapps.presentation.ui.screen.Setting.ThemeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    )

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
    )

@Composable
fun PomodoroAppsTheme(themeSwitch: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (themeSwitch) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
){
    val context = LocalContext.current
    val viewModel = remember { ThemeViewModel(context.dataStore) }
    val state = viewModel.state.observeAsState()
    val value = state.value ?: isSystemInDarkTheme()

    LaunchedEffect(viewModel) { viewModel.request() }

    DarkThemeValue.current.value = value
    MaterialTheme(
        colors = if (value) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
@ReadOnlyComposable
fun isDarkTheme() = DarkThemeValue.current.value

@SuppressLint("CompositionLocalNaming")
private val DarkThemeValue = compositionLocalOf { mutableStateOf(false) }


