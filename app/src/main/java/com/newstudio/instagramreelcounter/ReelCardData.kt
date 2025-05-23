package com.newstudio.instagramreelcounter

import android.graphics.drawable.AdaptiveIconDrawable
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class ReelCardData(
    val appName: String,
    val reelCount: Long,
    val watchHours: Float,
    val iconResId: Int,
    val cardColor : List<Color>
)