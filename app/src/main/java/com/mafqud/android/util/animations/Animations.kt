package com.mafqud.android.util.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mafqud.android.R
import com.mafqud.android.ui.theme.IconUi
import com.mafqud.android.ui.theme.green
import kotlinx.coroutines.delay


@Composable
fun WavesAnimation(modifier: Modifier) {

    val waves = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        //remember { Animatable(0f) },
    )

    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(4000, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart,
    )

    waves.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 800L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = animationSpec
            )
        }
    }

    val dys = waves.map { it.value }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Waves
        dys.forEach { dy ->
            Box(
                Modifier
                    .size(35.dp)
                    .padding(4.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = dy * 2 + 1
                        scaleY = dy * 2 + 1
                        alpha = 1 - dy
                    },
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.green.copy(
                            alpha = 0.2f
                        ), shape = CircleShape)
                )
            }
        }

        // Mic icon
        Box(
            Modifier
                .size(40.dp)
                .padding(8.dp)
                .align(Alignment.Center)
                .background(color = Color.White, shape = CircleShape)
        ) {
            IconUi(
                painter = painterResource(id = R.drawable.ic_phone),
                "",
                tint = MaterialTheme.colorScheme.green,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }

    }

}