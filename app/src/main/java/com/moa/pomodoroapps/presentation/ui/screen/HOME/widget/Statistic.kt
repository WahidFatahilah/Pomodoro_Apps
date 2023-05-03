package com.moa.pomodoroapps.presentation.ui.screen.HOME.widget

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.*

@Composable
fun Statistic(totalTask: Int, totalTaskDone: Int, totalPercentage: Int) {
    Card(
        shape = RoundedCornerShape(19.dp),
        modifier = Modifier
            .fillMaxWidth(1f)
            .heightIn(min = 30.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.Pink,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {

                Card(
                    modifier = Modifier
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = MaterialTheme.colors.PinkSoft
                ) {
                    Column(
                        modifier = Modifier
                            .heightIn(30.dp, 100.dp)
                            .fillMaxWidth()
                            .padding(
                                start = 20.dp,
                                top = 6.dp,
                                end = 20.dp,
                                bottom = 6.dp
                            ), verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = stringResource(R.string.total_task),
                            style = Heading_H2,
                            color = asset_White,
                            modifier = Modifier
                        )
                        Text(
                            text = totalTask.toString() + stringResource(id = R.string.task),
                            //"0 Project",
                            style = Ket_2,
                            color = asset_White,
                            modifier = Modifier
                        )
                    }


                }

                Card(
                    modifier = Modifier
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = MaterialTheme.colors.PinkSoft
                ) {
                    Column(
                        modifier = Modifier
                            .heightIn(30.dp, 100.dp)
                            .fillMaxWidth()
                            .padding(
                                start = 20.dp,
                                top = 6.dp,
                                end = 20.dp,
                                bottom = 6.dp
                            ), verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = stringResource(R.string.task_done),
                            style = Heading_H2,
                            color = asset_White,
                            modifier = Modifier

                        )
                        Text(
                            text = totalTaskDone.toString() + stringResource(id = R.string.task),
                            style = Ket_2,
                            color = asset_White,
                            modifier = Modifier
                        )
                    }


                }

            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(150.dp, 250.dp),
                ) {

                    var circleCenter = Offset.Zero

                    var positionValue = totalPercentage

                    val width = size.width
                    val height = size.height
                    val circleThickness = width / 10f
                    circleCenter = Offset(x = width / 2f, y = height / 2f)
                    var maxValue: Int = 100

                    drawCircle(
                        color = PinkSoft,
                        radius = 100f - 30f,
                        center = circleCenter
                    )

                    drawCircle(
                        style = Stroke(
                            width = circleThickness
                        ),
                        color = asset_White,
                        radius = 100f,
                        center = circleCenter,

                        )


                    drawArc(
                        color = Color.Yellow,
                        startAngle = -90f,
                        sweepAngle = (360f / maxValue) * positionValue.toFloat(),
                        style = Stroke(
                            width = circleThickness,
                            cap = StrokeCap.Round

                        ),
                        useCenter = false,
                        size = Size(
                            width = 100f * 2f,
                            height = 100f * 2f
                        ),
                        topLeft = Offset(
                            (width - 100f * 2f) / 2f,
                            (height - 100f * 2f) / 2f
                        )
                    )
                    drawContext.canvas.nativeCanvas.apply {
                        drawIntoCanvas {
                            drawText(
                                "$positionValue%",
                                circleCenter.x,
                                circleCenter.y + 45.dp.toPx() / 8f,
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

    }
}