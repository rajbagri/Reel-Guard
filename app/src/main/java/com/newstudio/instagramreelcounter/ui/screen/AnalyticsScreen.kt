package com.newstudio.instagramreelcounter.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newstudio.instagramreelcounter.ui.components.BarChartGraph
import com.newstudio.instagramreelcounter.ui.components.ReelUsageTimeline

@Composable
fun AnalyticsScreen(values : PaddingValues) {
    val data = listOf(1700f, 300f, 1600f, 1800f, 1500f, 2400f, 100f)
    val labels = listOf("M", "T", "W", "T", "F", "S", "S")

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = values.calculateTopPadding())
    ){
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
        ){
            Text(
                modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 20.dp),
                text = "Analytics",
                color = Color.White,
                fontSize = 20.sp
            )
            BarChartGraph(
                values = data,
                labels = labels,
                maxValue = 2700f
            )
        }



        Text(
            "Daily Usage Timeline",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp)
        )

        val times = listOf("9 AM", "12 PM", "3 PM", "6 PM")
        val reels = listOf(10, 15, 12, 8)
        val engagement = listOf(30, 45, 40, 25)  // in minutes

        ReelUsageTimeline(times, reels, engagement)



    }

}