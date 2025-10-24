package com.pedromassango.compose_concepts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import compose_concepts.composeapp.generated.resources.Res
import compose_concepts.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    var alphaEnabled by remember { mutableStateOf(false) }
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            InfiniteGlobeAnimation(
                alphaEnabled = alphaEnabled
            )

            IconToggleButton(
                checked = alphaEnabled,
                onCheckedChange = {alphaEnabled = it}
            ) {
                Text(alphaEnabled.toString())
            }
        }
    }
}