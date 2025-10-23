package com.pedromassango.compose_concepts

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose_concepts",
    ) {
        App()
    }
}