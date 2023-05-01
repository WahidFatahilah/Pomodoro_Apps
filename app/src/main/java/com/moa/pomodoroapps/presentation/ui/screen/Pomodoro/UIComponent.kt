package com.moa.pomodoroapps.presentation.ui.screen.Pomodoro

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun SessionIcons(sessionCount: Int) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (sessionIndex in 1..3) {
            val isCompleted = sessionIndex <= sessionCount
            iconForSession(sessionIndex, isCompleted)
        }
    }
}

@Composable
private fun iconForSession(sessionIndex: Int, isCompleted: Boolean) {
    var sessionCount = sessionIndex
    val icon: ImageVector = when {
        isCompleted -> Icons.Filled.CheckCircle
        sessionIndex == sessionCount + 1 -> Icons.Default.Lock
        else -> Icons.Filled.Lock
    }

    Icon(
        imageVector = icon,
        contentDescription = "Session $sessionIndex",
        modifier = Modifier.padding(horizontal = 8.dp),
        tint = if (isCompleted) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium)
    )
}

