package com.newstudio.instagramreelcounter

import android.graphics.drawable.Drawable

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable?,
    var totalTimeMillis: Long,
    var limitHours: Int = 0,
    var limitMinutes: Int = 0
)