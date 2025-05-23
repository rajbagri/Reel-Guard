package com.newstudio.instagramreelcounter.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
fun BarChartGraph(
    values: List<Float>,
    labels: List<String>,
    maxValue: Float
) {
    val chartHeight = 180.dp
    val barWidth = 32.dp
    val animatedProgress = remember { mutableStateListOf<Float>() }
    val selectedBarIndex = remember { mutableStateOf(-1) } // -1 means no selection

    LaunchedEffect(Unit) {
        animatedProgress.clear()
        repeat(values.size) {
            animatedProgress.add(0f)
        }
        values.forEachIndexed { i, value ->
            delay(120)
            animatedProgress[i] = value / maxValue
        }
    }

    val yAxisSteps = 4
    val yAxisValues = List(yAxisSteps + 1) { i ->
        ((maxValue / yAxisSteps) * (yAxisSteps - i)).toInt()
    }



    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp).padding(bottom = 20.dp)
    ) {

        Column(
            modifier = Modifier
                .height(chartHeight+40.dp)
                .padding(end = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            yAxisValues.forEach {
                Text(
                    text = it.toString(),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight + 60.dp)
        ) {
            // Grid Lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (i in 0..yAxisSteps-1) {
                    val y = size.height * i / yAxisSteps
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                values.forEachIndexed { index, value ->
                    val progress by animateFloatAsState(
                        targetValue = animatedProgress.getOrElse(index) { 0f },
                        animationSpec = androidx.compose.animation.core.tween(durationMillis = 600),
                        label = "barAnim$index"
                    )

                    val heightDp = with(LocalDensity.current) {
                        (chartHeight.toPx() * progress).toDp()
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedBarIndex.value = if (selectedBarIndex.value == index) -1 else index }
                    ) {
                        // Value shown only when selected
                        if (selectedBarIndex.value == index) {
                            Text(
                                text = value.toInt().toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        } else {
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        Box(
                            modifier = Modifier
                                .height(heightDp)
                                .width(barWidth)
                                .shadow(4.dp, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF37474F),
                                            Color(0xFF263238),
                                        ) // Deep green and dark teal
                                    ),
                                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                                )
                        )

                        HorizontalDivider(
                            modifier = Modifier.width(barWidth + 10.dp),
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = labels[index],
                            color = Color.Gray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlowingBarChartPreview() {
    Column {
        Spacer(modifier = Modifier.height(60.dp)) // Push below TopAppBar
        BarChartGraph(
            values = listOf(1700f, 300f, 1600f, 1800f, 1500f, 2600f, 100f),
            labels = listOf("M", "T", "W", "T", "F", "S", "S"),
            maxValue = 3000f
        )
    }
}
