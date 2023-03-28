package com.moa.pomodoroapps.ui.feature.appbarcustom.topnav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.pomodoroapps.ui.theme.FontColor
import com.moa.pomodoroapps.ui.theme.Heading_H1
import com.moa.pomodoroapps.ui.theme.Ket_2
import com.moa.pomodoroapps.ui.theme.backgroundColor

@Composable
fun CustomTopAppBar(
//    icon: Painter,
//    onIconClick: () -> Unit,
    title: String = "Pomodoro Timer",
    subtitle: String = "be productive today!",
//    actionIcon: Painter,
//    onActionClick: () -> Unit,
) {
    TopAppBar(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.backgroundColor,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                IconButton(onClick = onIconClick) {
//                    Icon(
//                        painter = icon,
//                        contentDescription = null,
//                        tint = MaterialTheme.colors.Pink,
//                           )
//                }
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title,
                        style = Heading_H1 ,
                        color = MaterialTheme.colors.FontColor
                    )
                    Text(
                        text = subtitle,
                        style = Ket_2,
                        color = MaterialTheme.colors.FontColor
                    )
                }
//                IconButton(
//                    onClick = onActionClick,
//                    modifier = Modifier.padding(start = 115.12.dp)
//                ) {
//                    Icon(
//                        painter = actionIcon,
//                        contentDescription = null,
//                        tint = MaterialTheme.colors.FontColor
//                    )
//                }
            }
        }
    )
}



@Preview
@Composable
fun PreviewCustomTopAppBar() {
    CustomTopAppBar()

}



// RESOUSRCE LAIN
/*   //TopBar
 Scaffold() {
     Row(
         modifier = Modifier
             .fillMaxWidth()
             .padding(top = 40.dp, start = 16.dp, end = 21.dp),
         horizontalArrangement = Arrangement.Center
     ) {
         Column(
             modifier = Modifier
                 .padding(start = 4.17.dp, top = 4.17.dp, bottom = 4.17.dp)
         ) {
             IconButton(onClick = { /*TODO*/ }) {
                 Icon(painter = painterResource(id = R.drawable.icon_profile_png),
                      contentDescription = "ProfileUser",
                      tint = asset_pink
                 )
             }
         }
         Column(
             modifier = Modifier
                 .padding(start = 14.7.dp, top = 5.dp, bottom = 18.dp),
             horizontalAlignment = Alignment.Start,

             ) {
             Row() {
                 Text(text = "Pomodoro Timer.", style = Heading_H1)
             }
             Row() {
                 Text(text = "be productive today!", style = Ket_2, )
             }
         }
         Column(
             modifier = Modifier
                 .padding(start = 84.13.dp, top = 15.08.dp,
                          end = 3.13.dp, bottom = 13.04.dp)
                 .width(18.75.dp)
                 .height(21.88.dp)
         ) {
             IconButton(onClick = { /*TODO*/ }) {
                 Icon(painter = painterResource(id = R.drawable.notifikasi_),
                      contentDescription = "Notification",
                      modifier = Modifier
                          .width(18.75.dp)
                          .height(21.88.dp)



                 )
             }
         }

     } //Akhir Top bar

     Row(
        modifier = Modifier.padding(start = 16.dp)
     ) {
         Text(text = "Proyek Hari Ini")
     }
 }
*/

