package com.moa.pomodoroapps.presentation.navigation.bottomnav

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.moa.pomodoroapps.presentation.ui.theme.Grey
import com.moa.pomodoroapps.presentation.ui.theme.Pink
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Pomodoro,
        BottomBarScreen.Statistik,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(

        backgroundColor = MaterialTheme.colors.backgroundColor,
        elevation = 10.dp
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
){

    val selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    BottomNavigationItem(
        label = {
            Text(
                text = screen.title,
                color = if (selected) MaterialTheme.colors.Pink
                else  MaterialTheme.colors.Grey
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Nav Icon",
                tint = if (selected) MaterialTheme.colors.Pink
                else  MaterialTheme.colors.Grey

            )
        },
        selected = selected,

        selectedContentColor = MaterialTheme.colors.Pink,
        onClick = {
            navController.navigate(screen.route)
        }
    )
}
