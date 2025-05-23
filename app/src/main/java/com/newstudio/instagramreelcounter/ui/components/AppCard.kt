package com.newstudio.instagramreelcounter.ui.components

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newstudio.instagramreelcounter.AppUsageInfo
import com.newstudio.instagramreelcounter.DataStoreManager
import com.newstudio.instagramreelcounter.R
import kotlinx.coroutines.launch

@Composable
fun AppCard(
    appUsageEntry: AppUsageInfo,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = DataStoreManager(context)
    var showDialog by remember { mutableStateOf(false) }
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }

    LaunchedEffect(appUsageEntry.packageName) {
        dataStore.getAppLimit(appUsageEntry.packageName).collect { (h, m) ->
            Log.d("time", "${h} : ${m}")
            hours = h
            minutes = m
        }
    }

    if (showDialog) {
        TimePicker(
            onDismiss = { showDialog = false },
            onTimeSelected = { hrs, mnts ->
                showDialog = false
                appUsageEntry.limitHours = hrs
                appUsageEntry.limitMinutes = mnts
                scope.launch {
                    dataStore.saveAppLimit(appUsageEntry.packageName, hrs, mnts)
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Icon
            Row {
                appUsageEntry.appIcon?.let { icon ->
                    Image(
                        painter = rememberDrawablePainter(icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // App Name and Usage Time
                Column {
                    Text(
                        text = appUsageEntry.appName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatTime(appUsageEntry.totalTimeMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Column(
                modifier = Modifier.padding(top = 8.dp, end = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .height(20.dp)
                        .clickable(
                            onClick = {
                                showDialog = !showDialog
                            }
                        ),
                    painter = painterResource(R.drawable.sandclick),
                    contentDescription = "",
                    tint = Color.Gray
                )

                if(hours > 0 || minutes > 0){
                    Text(
                        modifier = Modifier,
                        text = "${appUsageEntry.limitHours}h : ${appUsageEntry.limitMinutes}m",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }


    }
}

@Composable
fun rememberDrawablePainter(drawable: Drawable): Painter {
    return remember(drawable) {
        BitmapPainter(drawable.toBitmap().asImageBitmap())
    }
}

fun formatTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return when {
        hours > 0 -> "${hours}h ${minutes % 60}m"
        minutes > 0 -> "${minutes}m ${seconds % 60}s"
        else -> "${seconds}s"
    }
}
