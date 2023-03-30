package com.adidongs.cobapomodoro.ui.feature.appbarcustom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    initialProjectValue: Int,
    initialTaskValue: Int,
) {
    var projectValue by remember {
        mutableStateOf(initialProjectValue)
    }

    var taskValue by remember {
        mutableStateOf(initialTaskValue)
    }


    Card(
        shape = RoundedCornerShape(19.dp),
        modifier = Modifier
            .size(328.dp, 208.dp),
        backgroundColor = MaterialTheme.colors.Pink,
    ) {
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
                   text = "$initialProjectValue Project",
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
                    text = "$initialTaskValue Task",
                    //"0 Tugas",
                    style = Ket_2,
                    color = asset_White,
                    modifier = Modifier
                        .padding(top = 37.dp, start = 12.dp, end = 80.dp, bottom = 7.dp),



                    )
            }
        }
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

        //progreess bar
       /* Canvas(
            modifier = Modifier
                .padding(216.dp,95.dp,16.dp,17.dp)
        ){
            drawCircle(
                color = asset_line,
                radius = 120f,
            )
            drawCircle(
                    color = asset_pink,
            radius = 120f-46f,
            )

            drawCircle(
                color = asset_light_pink,
                radius = 120f-60f,
            )

    } */

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



@Preview
@Composable
fun PreviewCard1(){
    CardHome(
        initialProjectValue = 4,
        initialTaskValue = 9
    )
}