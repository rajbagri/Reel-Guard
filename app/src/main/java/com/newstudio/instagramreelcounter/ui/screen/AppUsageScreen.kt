package com.newstudio.instagramreelcounter.ui.screens

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newstudio.instagramreelcounter.AppUsageInfo
import com.newstudio.instagramreelcounter.DataStoreManager
import com.newstudio.instagramreelcounter.ui.components.AppCard
import com.newstudio.instagramreelcounter.ui.components.PieChart
import com.newstudio.instagramreelcounter.ui.components.formatTime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppUsageScreen(values: PaddingValues) {
    val context = LocalContext.current
    var usageList by remember { mutableStateOf<List<AppUsageInfo>>(emptyList()) }

    LaunchedEffect(Unit) {
        usageList = getAppUsageList(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = values.calculateTopPadding())
    ) {
        if (usageList.isEmpty()) {
            Text(
                text = "No app usage data available.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            val topCount = 3
            val topApps = usageList.take(topCount)
            val othersUsage = if (usageList.size > topCount) {
                usageList.drop(topCount).sumOf { it.totalTimeMillis }
            } else 0L

            val paddedTopApps = topApps + List(topCount - topApps.size) {
                AppUsageInfo("", "Unused", null, 0)
            }

            val totalUsage = (paddedTopApps.sumOf { it.totalTimeMillis } + othersUsage).takeIf { it > 0 } ?: 1f

            val segments = paddedTopApps.map { it.totalTimeMillis / totalUsage.toFloat() } +
                    listOf(othersUsage / totalUsage.toFloat())

            val labels = paddedTopApps.map {
                it.appName to formatTotalTime(it.totalTimeMillis)
            } + ("Others" to formatTotalTime(othersUsage))

            val colors = listOf(
                Color(0xFFFF2DAA),
                Color(0xFF833AB4),
                Color(0xFFFF0000),
                Color(0xFF1877F2)
            )

            LazyColumn {
                item {
                    PieChart(
                        segments = segments,
                        colors = colors,
                        centerTextTop = "Time Wasted",
                        centerTextBottom = formatTotalTime(paddedTopApps.sumOf { it.totalTimeMillis } + othersUsage),
                        appLabels = labels
                    )

                    Text(
                        text = "App Usage",
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 10.dp),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(usageList) { entry ->
                    AppCard(appUsageEntry = entry)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun getAppUsageList(context: Context): List<AppUsageInfo> {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val packageManager = context.packageManager
    val dataStore = DataStoreManager(context)

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startTime = calendar.timeInMillis
    val endTime = System.currentTimeMillis()

    val usageStatsList: List<UsageStats> =
        usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)

    val appUsageList = mutableListOf<AppUsageInfo>()

    for (usageStats in usageStatsList) {
        val packageName = usageStats.packageName
        val totalTime = usageStats.totalTimeInForeground

        if (totalTime > 0) {
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val appIcon: Drawable = packageManager.getApplicationIcon(appInfo)

                val (limitHours, limitMinutes) = dataStore.getAppLimit(packageName).first()

                appUsageList.add(
                    AppUsageInfo(
                        packageName = packageName,
                        appName = appName,
                        appIcon = appIcon,
                        totalTimeMillis = totalTime,
                        limitHours = limitHours,
                        limitMinutes = limitMinutes
                    )
                )
            } catch (e: PackageManager.NameNotFoundException) {
                // skip unknown app
            }
        }
    }

    return appUsageList.sortedByDescending { it.totalTimeMillis }
}

fun formatTotalTime(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return when {
        seconds < 60 -> "${seconds}s"
        minutes < 60 -> "${minutes}m"
        else -> String.format("%.1fh", hours + (minutes % 60) / 60.0)
    }
}