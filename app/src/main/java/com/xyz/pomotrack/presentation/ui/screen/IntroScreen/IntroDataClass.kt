package com.xyz.pomotrack.presentation.ui.screen.IntroScreen

import androidx.annotation.DrawableRes
import com.xyz.pomotrack.R


data class OnboardContent(
    val title: String,
    val subtitle: String,
    @DrawableRes val imageRes: Int
)

val onboardContents = listOf(
    OnboardContent(
        title = "Focus with intention",
        subtitle = "Plan your day into clean focus sessions, quick breaks, and steady progress.",
        imageRes = R.drawable.ic_done
    ),
    OnboardContent(
        title = "Track what matters",
        subtitle = "See tasks, time used, and productivity in one calm dashboard.",
        imageRes = R.drawable.deletelogo
    ),
    OnboardContent(
        title = "Build momentum daily",
        subtitle = "Stay consistent with a simple flow that makes starting easier every day.",
        imageRes = R.drawable.ic_dont_play
    ),
)
