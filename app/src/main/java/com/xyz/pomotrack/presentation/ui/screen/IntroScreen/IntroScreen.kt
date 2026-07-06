package com.xyz.pomotrack.presentation.ui.screen.IntroScreen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.xyz.pomotrack.OnboardingDataStore
import com.xyz.pomotrack.R
import com.xyz.pomotrack.presentation.ui.theme.Heading_H1
import com.xyz.pomotrack.presentation.ui.theme.Heading_H2
import com.xyz.pomotrack.presentation.ui.theme.Pink
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_2
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroScreen(modifier: Modifier, navController: NavHostController) {
    val pagerState = rememberPagerState()
    val contents = onboardContents
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = remember { OnboardingDataStore(context) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7FBFF), Color(0xFFFFF6F8), Color(0xFFF8F2FF))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 52.dp, start = 28.dp)
                .size(110.dp)
                .clip(CircleShape)
                .background(Color(0x1AF74678))
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 120.dp, end = 18.dp)
                .size(150.dp)
                .clip(CircleShape)
                .background(Color(0x12A58AFF))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Pomodoro Apps",
                    style = Heading_H2,
                    color = Color(0xFF1F1633),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "A calmer way to build focus every day",
                    style = Subtitle_2,
                    color = Color(0xFF6A647A)
                )

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalPager(
                    count = contents.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    OnboardPage(content = contents[page], position = page)
                }

                Spacer(modifier = Modifier.height(16.dp))

                SliderIndicator(
                    pageSize = contents.size,
                    selectedPage = pagerState.currentPage,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage == contents.lastIndex) {
                                dataStore.setOnboardingCompleted(true)
                                navController.navigate("mainScreen")
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1, 0f)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Pink),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp)
                ) {
                    val buttonText = stringResource(
                        if (pagerState.currentPage != contents.lastIndex) {
                            R.string.action_continue
                        } else {
                            R.string.action_start
                        }
                    )
                    Crossfade(targetState = buttonText) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.button,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Create flow, keep momentum, and finish more with less friction.",
                    color = Color(0xFF716A80),
                    style = Subtitle_2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun SliderIndicator(
    modifier: Modifier = Modifier,
    pageSize: Int,
    selectedPage: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (0 until pageSize).forEach { position ->
            val width by animateDpAsState(if (position == selectedPage) 22.dp else 8.dp)
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(if (position == selectedPage) Pink else Color(0x33F74678))
            )
        }
    }
}

@Composable
fun OnboardPage(modifier: Modifier = Modifier, content: OnboardContent, position: Int) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        backgroundColor = Color.White,
        elevation = 0.dp,
        border = BorderStroke(1.dp, Color(0x14F74678))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFFE3EA), Color(0xFFF5E9FF), Color(0xFFEAF7FF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.30f))
                )
                Box(
                    modifier = Modifier
                        .size(176.dp)
                        .clip(RoundedCornerShape(44.dp))
                        .background(Color.White.copy(alpha = 0.52f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = content.imageRes),
                        modifier = Modifier.size(110.dp),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
                Canvas(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(18.dp)
                        .size(84.dp)
                ) {
                    drawCircle(color = Color.White.copy(alpha = 0.55f), radius = size.minDimension / 2f)
                    drawCircle(
                        color = Color(0xFFF74678),
                        radius = size.minDimension / 2.8f,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color(0xFFFCE3EA)
            ) {
                Text(
                    text = "Daily focus",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    color = Color(0xFFB93D64),
                    style = Subtitle_2
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = content.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Heading_H1,
                color = Color(0xFF1F1633)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content.subtitle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Heading_H2,
                lineHeight = 22.sp,
                color = Color(0xFF716A80)
            )

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
