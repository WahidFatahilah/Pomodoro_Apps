package com.moa.pomodoroapps.presentation.ui.feature.appbarcustom.system

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moa.pomodoroapps.presentation.ui.theme.PinkSoft
import com.moa.pomodoroapps.presentation.ui.theme.Yellow
import com.moa.pomodoroapps.presentation.ui.theme.asset_White
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor

@Composable
fun CustomCircularBar(
    modifier: Modifier = Modifier,
    initialValue: Int,
    primaryColor: Color,
    secondaryColor: Color,
    maxValue: Int = 100,
    circleRadius: Float

){
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var positionValue by remember {
        mutableStateOf(initialValue)
    }

    Box(
        modifier = modifier
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
        ){
            val width = size.width
            val height = size.height
            val circleThickness = width/10f
            circleCenter = Offset(x = width/2f, y = height/2f)

            drawCircle(
                color = PinkSoft,
                radius = circleRadius - 30f ,
                center = circleCenter
            )

            drawCircle(
                style = Stroke(
                    width = circleThickness
                ),
                color = secondaryColor,
                radius = circleRadius,
                center = circleCenter,

            )


            drawArc(
                color = primaryColor,
                startAngle = -90f,
                sweepAngle = (360f/maxValue) * positionValue.toFloat(),
                style = Stroke(
                    width = circleThickness,
                    cap = StrokeCap.Round

                ),
                useCenter = false,
                size = Size(
                    width = circleRadius * 2f,
                    height= circleRadius * 2f
                ),
                topLeft = Offset(
                    (width - circleRadius * 2f)/2f,
                    (height - circleRadius * 2f)/2f
                )
            )
        drawContext.canvas.nativeCanvas.apply {
            drawIntoCanvas {
                drawText(
                    "$positionValue%",
                    circleCenter.x,
                    circleCenter.y + 45.dp.toPx()/8f,
                    Paint().apply {
                        textSize = 16.sp.toPx()
                        textAlign = Paint.Align.CENTER
                        color = asset_White.toArgb()
                        isFakeBoldText = true
                    }
                )
            }
        }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview(){
    CustomCircularBar(
        modifier = Modifier
            .size(250.dp)
            .background(color = MaterialTheme.colors.backgroundColor),
        initialValue = 78,
        primaryColor = MaterialTheme.colors.Yellow,
        secondaryColor = Color.LightGray,
        circleRadius = 120f
    )
}
