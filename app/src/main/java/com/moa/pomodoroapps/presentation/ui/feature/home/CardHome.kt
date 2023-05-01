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
    totalTask: Int,
    totalTaskDone: Int,
) {
    var TotalTask by remember {
        mutableStateOf(totalTask)
    }
    var TotalTaskDone by remember {
        mutableStateOf(totalTaskDone)
    }
    var Percentage by remember {
        mutableStateOf(0.0)
    }

    Percentage = ((TotalTask / TotalTaskDone) * 100).toDouble()
    var  TotalPercentage = Percentage.toInt()



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
                    Column(modifier = Modifier
                        .heightIn(30.dp, 100.dp)
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 6.dp, end = 20.dp, bottom = 6.dp), verticalArrangement = Arrangement.Top) {
                        Text(
                            text = "Total Tugas ",
                            style = Heading_H2,
                            color = asset_White,
                            modifier = Modifier
                          //     .padding(top = 7.dp, start = 12.dp, end = 31.dp, bottom = 31.dp),

                            )
                        Text(
                            text = "$TotalTask Project",
                            //"0 Project",
                            style = Ket_2,
                            color = asset_White,
                            modifier = Modifier
                               // .padding(top = 37.dp, start = 12.dp, end = 80.dp, bottom = 7.dp),



                            )
                    }


                }

            Card(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = MaterialTheme.colors.PinkSoft
            ) {
                Column(modifier = Modifier
                    .heightIn(30.dp, 100.dp)
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 6.dp, end = 20.dp, bottom = 6.dp), verticalArrangement = Arrangement.Top) {
                    Text(
                        text = "Total Tugas Selesai ",
                        style = Heading_H2,
                        color = asset_White,
                        modifier = Modifier
                        //     .padding(top = 7.dp, start = 12.dp, end = 31.dp, bottom = 31.dp),

                    )
                    Text(
                        text = "$TotalTaskDone Project",
                        //"0 Project",
                        style = Ket_2,
                        color = asset_White,
                        modifier = Modifier
                        // .padding(top = 37.dp, start = 12.dp, end = 80.dp, bottom = 7.dp),



                    )
                }


            }

        }

/*        CustomCircularBar(
            modifier = Modifier .fillMaxWidth()
            ,
            initialValue = Percentage, //di ganti get data nanti
            primaryColor = MaterialTheme.colors.Yellow,
            secondaryColor = asset_White,
            circleRadius = 100f,
        )*/

    }

}


}


@Preview
@Composable
fun PreviewCard1(){
/*    CardHome(
        totalTask = 4,
        totalTaskDone = 3,
    )*/
}