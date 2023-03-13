package com.moa.pomodoroapps

import android.graphics.Paint.Style
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily


@Composable
fun HomeScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()){
            Text(text = "Primary Semi Bold")
            Text(text = "Header H1 ")
            Text(text = "Header H2")
            Text(text = "Subtitle 1")
            Text(text = "Subtitle 2")
            Text(text = "Keterangan 1 ")
            Text(text = "Keterangan 2 ")






        }

    }

}
