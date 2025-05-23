package com.newstudio.instagramreelcounter.ui.animatewrapper

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedCardWrapper(
    index: Int,
    delayMillis: Int = 100,
    content: @Composable () -> Unit
) {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.8f) }
    val rotationYAnim = remember { Animatable(45f) }
    val translationYAnim = remember { Animatable(30f) }

    val density = LocalDensity.current.density

    LaunchedEffect(Unit) {
        delay(index * delayMillis.toLong())
        launch { alphaAnim.animateTo(1f, tween(600)) }
        launch { scaleAnim.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
        launch { rotationYAnim.animateTo(0f, tween(700)) }
        launch { translationYAnim.animateTo(0f, tween(700)) }
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                rotationY = rotationYAnim.value
                translationY = translationYAnim.value
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
                alpha = alphaAnim.value
                cameraDistance = 12f * density
            }
    ) {
        content()
    }
}

