package com.adidongs.cobapomodoro.ui.feature.appbarcustom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.presentation.ui.feature.appbarcustom.system.CustomCircularBar
import com.moa.pomodoroapps.presentation.ui.theme.*



@Composable
fun CardHome(
    totalProject: Int,
    totalProjectDone: Int,
) {
    var TotalTask by remember {
        mutableStateOf(totalProject)
    }
    var TotalTaskDone by remember {
        mutableStateOf(totalProjectDone)
    }


    Card(
        shape = RoundedCornerShape(19.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(208.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.Pink,
    ) {
        Row() {
            //card1
            Column() {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(144.dp, 62.dp),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = MaterialTheme.colors.PinkSoft
                ) {
                    Text(
                        text = "Total Proyek",
                        style = Heading_H2,
                        color = asset_White,
                        modifier = Modifier
                            .padding(top = 7.dp, start = 12.dp, end = 31.dp, bottom = 31.dp),

                        )
                    Text(
                        text = "$TotalTask Project",
                        //"0 Project",
                        style = Ket_2,
                        color = asset_White,
                        modifier = Modifier
                            .padding(top = 37.dp, start = 12.dp, end = 80.dp, bottom = 7.dp),



                        )
                }
            }
            //card2
            Column() {
                Card(
                    modifier = Modifier
                        .padding(172.dp, 16.dp, 12.dp, 0.dp)
                        .size(144.dp, 62.dp),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = MaterialTheme.colors.PinkSoft
                ) {
                    Text(
                        text = "Total Tugas",
                        style = Heading_H2,
                        color = asset_White,
                        modifier = Modifier
                            .padding(top = 7.dp, start = 12.dp, end = 31.dp, bottom = 31.dp),

                        )
                    Text(
                        text = "$TotalTaskDone Task",
                        //"0 Tugas",
                        style = Ket_2,
                        color = asset_White,
                        modifier = Modifier
                            .padding(top = 37.dp, start = 12.dp, end = 80.dp, bottom = 7.dp),



                        )
                }
            }
        }

        Row(){
            //card 3
            Column() {
                Card(
                    modifier = Modifier
                        .padding(top = 100.dp, start = 16.dp)
                        .size(191.dp, 85.dp),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = MaterialTheme.colors.PinkSoft
                ) {
                    Text(
                        text =  "Progress Berjalan",
                        style = Heading_H2,
                        color = asset_White,
                        modifier = Modifier
                            .padding(top = 7.dp, start = 12.dp, end = 31.dp, bottom = 31.dp),

                        )
                    Text(
                        text = /* ${getdata pake if}*/
                        "kamu belum memulai,       ayo tambahkan tugasmu ",
                        maxLines = 2,
                        style = Ket_2,
                        color = asset_White,
                        modifier = Modifier
                            .padding(top = 37.dp, start = 12.dp, end = 0.dp, bottom = 0.dp),

                        )
                }
            }


            CustomCircularBar(
                modifier = Modifier
                    .padding(216.dp, 95.dp, 16.dp, 17.dp)
                    .size(250.dp)
                ,
                initialValue = 50, //di ganti get data nanti
                primaryColor = MaterialTheme.colors.Yellow,
                secondaryColor = asset_White,
                circleRadius = 100f,
            )
        }

    }
}

@Preview
@Composable
fun PreviewCard1(){
    CardHome(
        totalProjectDone = 4,
        totalProject = 3,
    )
}