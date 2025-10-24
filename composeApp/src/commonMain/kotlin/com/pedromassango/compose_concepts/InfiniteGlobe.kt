package com.pedromassango.compose_concepts

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import io.kamel.core.utils.URI
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun Float.toRadians(): Float = (this * (PI / 180.0)).toFloat()

@Composable
fun InfiniteGlobeAnimation(
    duration: Int = 12000,
    alphaEnabled: Boolean,
    size: Dp = 450.dp,
    images: List<String> = List(100) {
        "https://i.pravatar.cc/150?u=${it}"
    }
) {
    val spherePoints = remember {
        List(images.size) { i ->
            // Use Fibonacci sphere algo for uniform distribution
            val phi = acos(1 - 2f * (i + 0.5f) / images.size) - (PI / 2)
            val theta = (PI * (1 + sqrt(5.0))) * i // golden angle
            Pair(phi.toFloat(), theta.toFloat())
        }
    }

    val hState = remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val baseRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing)
        ),
        label = ""
    )

    val r by remember {
        derivedStateOf {
            baseRotation + hState.value
        }
    }

    Box(
        modifier = Modifier.size(size)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { d ->
                    hState.value += (d * .1355f)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        spherePoints.forEachIndexed { index, (phi, thetaBase) ->
            val radius = size.value
            val theta = -(thetaBase + r.toRadians()) % (2 * PI).toFloat()

            // Project to 2D
            val x = (radius * cos(phi) * cos(theta))
            val y = (radius * sin(phi))
            val z = (radius * cos(phi) * sin(theta))

            // Perspective scaling (simulate depth)
            val scale = 0.3f + 0.7f * ((z / radius + 1f) / 2f)
            val depth = cos(scale)

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = x
                        translationY = y
                        scaleX = (scale * 1.95f) - depth
                        scaleY = (scale * 1.95f) - depth
                        if (alphaEnabled) {
                            alpha = (scale * 2) - .98f
                        }
                    }
                    .zIndex(z)
                    .clip(CircleShape)
                    .size(40.dp)
            ) {
                KamelImage(
                    resource = asyncPainterResource(URI(images[index])),
                    contentDescription = "Avatar of user $index",
                    modifier = Modifier.size(300.dp),
                    onLoading = {
                        CircularProgressIndicator()
                    },
                    onFailure = {
                        println(it.printStackTrace())
                        Text("(•_•)")
                    }
                )
                /*  AsyncImage(
                      model = images[index],
                      contentDescription = null,
                      filterQuality = FilterQuality.High
                  )*/

            }
        }
    }
}
