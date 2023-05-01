package com.moa.pomodoroapps.presentation.ui.screen.IntroScreen

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H1
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Pink
import com.moa.pomodoroapps.presentation.ui.theme.PinkSoft
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroScreen(modifier: Modifier, navController: NavController) {
    val pagerState = rememberPagerState()
    val currentState = pagerState.currentPage
    val contents = onboardContents
    val coroutineScope = rememberCoroutineScope()

    val mContext = LocalContext.current
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

        HorizontalPager(
            count = contents.size,
            modifier = Modifier.weight(1f),
            state = pagerState,
        ) { page ->
            OnboardPage(content = contents[page], position = page)
        }

        SliderIndicator(
            pageSize = contents.size,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(vertical = 16.dp),
            selectedPage = pagerState.currentPage
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage == contents.lastIndex)
                    {
                       navController.navigate("openOnboardingScreen")
                    } else if (currentState < contents.lastIndex) {
                        pagerState.animateScrollToPage(currentState + 1, 0f)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .padding(bottom = 48.dp)
                .height(58.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Pink),
            elevation = ButtonDefaults.elevation(1.dp, 4.dp, 0.dp),
        ) {
            val buttonText = stringResource(if (pagerState.targetPage != contents.lastIndex) R.string.action_continue else R.string.action_start)
            Crossfade(targetState = buttonText, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.button,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SliderIndicator(modifier: Modifier = Modifier, pageSize: Int, selectedPage: Int) {
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (0 until pageSize).forEach { position ->
            val width by animateDpAsState(if (position == selectedPage) 16.dp else 8.dp)
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(if (position == selectedPage) PinkSoft else Pink)
            )
        }
    }
}

@Composable
fun OnboardPage(modifier: Modifier = Modifier, content: OnboardContent, position: Int) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center
    ) {
        with(content) {
            val context = LocalContext.current
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()

             Image(
                 painter = painterResource(id = imageRes),
                 modifier = Modifier
                     .fillMaxWidth()
                     .clip(
                         RoundedCornerShape(
                             bottomEndPercent = if (position == onboardContents.lastIndex) 25 else 0,
                             bottomStartPercent = if (position == 0) 25 else 0
                         )
                     )
                     .background(PinkSoft)
                     .padding(horizontal = 72.dp)
                     .weight(1f)
                     .align(CenterHorizontally),
                 contentDescription = null,
                 contentScale = ContentScale.FillWidth,
                 alignment = Center
             )


            Column {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 48.dp),
                    textAlign = TextAlign.Center,
                    style = Heading_H1
                )

                Text(
                    text = subtitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp)
                        .padding(top = 20.dp),
                    textAlign = TextAlign.Center,
                    style = Heading_H2,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
