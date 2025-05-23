package com.newstudio.instagramreelcounter.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun PieChart(
    segments: List<Float>,
    colors: List<Color>,
    centerTextTop: String,
    centerTextBottom: String,
    appLabels: List<Pair<String, String>>, // e.g., [("Instagram", "58s"), ("YouTube", "35s")]
    modifier: Modifier = Modifier,
    strokeWidth: Float = 110f,
    backgroundColor: Color = Color.Transparent
) {
    val animatedSweepAngles = remember(segments) {
        segments.map { Animatable(0f) }
    }

    LaunchedEffect(Unit) {
        animatedSweepAngles.forEachIndexed { index, anim ->
            launch {
                anim.animateTo(
                    targetValue = segments[index] * 360f,
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.DarkGray.copy(alpha = 0.2f),
                        Color.DarkGray.copy(alpha = 0.4f),
                    )
                ),
                shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
            )
            .clip(RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(270.dp)
                        .padding(strokeWidth.dp / 4)
                ) {
                    val arcSize = Size(size.width+40, size.height+40)
                    var startAngle = -90f
                    val gapAngle = 25f

                    drawArc(
                        color = backgroundColor,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    animatedSweepAngles.forEachIndexed { index, animSweep ->
                        val sweepAngle = animSweep.value - gapAngle
                        if (sweepAngle > 0) {
                            drawArc(
                                color = colors.getOrElse(index) { Color.Transparent },
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                        startAngle += animSweep.value
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = centerTextTop, color = Color.Gray, fontSize = 14.sp)
                    Text(
                        text = centerTextBottom,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val itemsPerColumn = 2 // number of vertical items per column
            val chunks = appLabels.chunked(itemsPerColumn) // labels: List<Pair<String, String>>

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                chunks.forEachIndexed { chunkIndex, columnItems ->
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                    ) {
                        columnItems.forEachIndexed { itemIndex, (appName, timeSpent) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(
                                            color = colors.getOrNull(chunkIndex * itemsPerColumn + itemIndex) ?: Color.Gray,
                                            shape = RoundedCornerShape(50)
                                        )
                                )
                                val newAppName = if (appName.length > 11) appName.take(8) + "..." else appName
                                Text(
                                    text = newAppName,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = timeSpent,
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }




            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    val segments = listOf(0.6f, 0.4f) // Example: 60% Instagram, 40% YouTube
    val colors = listOf(Color(0xFFFF2DAA), Color(0xFFFF6F61)) // Pink, Coral
    val labels = listOf("Instagram" to "58s", "YouTube" to "35s")

    PieChart(
        segments = segments,
        colors = colors,
        centerTextTop = "Time Wasted",
        centerTextBottom = "1m",
        appLabels = labels
    )
}




