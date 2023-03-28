package com.moa.pomodoroapps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.moa.pomodoroapps.ui.feature.appbarcustom.bottomnav.BottomBar
import com.moa.pomodoroapps.ui.feature.appbarcustom.bottomnav.BottomNavGraph
import com.moa.pomodoroapps.ui.feature.appbarcustom.topnav.CustomTopAppBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moa.pomodoroapps.ui.theme.backgroundColor

@Preview
@ExperimentalPagerApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()


    Scaffold(
        backgroundColor = MaterialTheme.colors.backgroundColor,

        topBar = { CustomTopAppBar() },

        bottomBar = { BottomBar(navController = navController) },

        modifier = Modifier.padding(top = 40.dp)

        ) {
        //SCAFFOLD KONTEN
        BottomNavGraph(navController = navController)
    }
}


