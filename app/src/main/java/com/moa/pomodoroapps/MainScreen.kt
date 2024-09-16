package com.moa.pomodoroapps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.moa.pomodoroapps.presentation.navigation.bottomnav.BottomBar
import com.moa.pomodoroapps.presentation.navigation.bottomnav.BottomNavGraph
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.Data.Project
import com.moa.pomodoroapps.Data.ProjectDAO
import com.moa.pomodoroapps.presentation.navigation.bottomnav.BottomBarScreen
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Preview
@ExperimentalPagerApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        backgroundColor = MaterialTheme.colors.backgroundColor,
        // topBar = { CustomTopAppBar() },

        bottomBar = { BottomBar(navController = navController) },

        // modifier = Modifier.padding(top = 40.dp)
        ) {
        // SCAFFOLD KONTEN
        BottomNavGraph(navController = navController)
    }

}
