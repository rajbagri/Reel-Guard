package com.newstudio.instagramreelcounter.ui.animatewrapper

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

@Composable
fun AnimatedUsageCardWrapper(
    index: Int,
    content: @Composable () -> Unit
) {
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 100L) // staggered animation
        visible.value = true
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = slideInVertically(
            initialOffsetY = { -100 }, // Slide in from 100px above
            animationSpec = tween(500)
        ) + fadeIn(animationSpec = tween(500)),

        exit = slideOutVertically(
            targetOffsetY = { -100 },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        content()
    }
}
