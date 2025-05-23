package com.newstudio.instagramreelcounter.ui.components
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

@Composable
fun ReelUsageTimeline(
    times: List<String>,
    reelsCount: List<Int>,
    engagementMinutes: List<Int>
) {
    val maxReels = reelsCount.maxOrNull() ?: 0

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(times) { index, time ->

            val isPeak = reelsCount[index] == maxReels
            val targetColor = if (isPeak) Color(0xFF1E88E5) else Color(0xFF37474F)
            val animatedColor by animateColorAsState(
                targetValue = targetColor,
                animationSpec = tween(durationMillis = 800)
            )

            // Staggered entrance animation
            var visible by remember { mutableStateOf(false) }
            val scale = animateFloatAsState(
                targetValue = if (visible) 1f else 0.8f,
                animationSpec = tween(durationMillis = 500, delayMillis = index * 100, easing = FastOutSlowInEasing)
            ).value
            LaunchedEffect(Unit) {
                delay(index * 150L)
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.5f),
            ) {
                Card(
                    modifier = Modifier
                        .height(140.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp))
                        .clickable(
                            onClick = { },
                            onClickLabel = "Reel stats card",
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = animatedColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Text(
                                text = time,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${reelsCount[index]} reels",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${engagementMinutes[index]} min engaged",
                                fontSize = 14.sp,
                                color = Color(0xFFB0BEC5)
                            )
                        }

                        if (isPeak) {
                            // Pulse animation for peak glow
                            val infiniteTransition = rememberInfiniteTransition()
                            val pulseSize by infiniteTransition.animateFloat(
                                initialValue = 18f,
                                targetValue = 26f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(pulseSize.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(Color.Yellow, Color.Transparent),
                                            radius = 50f
                                        ),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}
