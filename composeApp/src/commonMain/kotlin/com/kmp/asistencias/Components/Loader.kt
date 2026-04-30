package com.kmp.asistencias.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import asistencias.composeapp.generated.resources.Res
import io.github.alexzhirkevich.compottie.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    fileName: String = "Loader_cat.json",
    delayMillis: Long = 1000L,
    onFinished: () -> Unit = {}
) {

    var showLoader by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            showLoader = true
        } else {
            delay(delayMillis)
            showLoader = false
            onFinished()
        }
    }

    if (showLoader) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            LottieLoader(
                fileName = fileName,
                modifier = Modifier.size(300.dp)
            )
        }
    }
}

@Composable
fun SuccessOverlay(isVisible: Boolean, onFinished: () -> Unit = {}) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            LottieLoader(
                fileName = "success.json",
                iterations = 1,
                modifier = Modifier.size(200.dp)
            )
        }
        
        LaunchedEffect(isVisible) {
            if (isVisible) {
                kotlinx.coroutines.delay(2000)
                onFinished()
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LottieLoader(
    fileName: String,
    iterations: Int = Compottie.IterateForever,
    modifier: Modifier = Modifier
) {
    var jsonText by remember(fileName) { mutableStateOf<String?>(null) }

    LaunchedEffect(fileName) {
        try {
            jsonText = Res.readBytes("files/$fileName").decodeToString()
        } catch (e: Exception) {
            println("Error loading lottie $fileName: ${e.message}")
        }
    }

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(jsonText ?: "{}")
    }
    
    val painter = rememberLottiePainter(
        composition = composition,
        iterations = iterations
    )

    Image(
        painter = painter,
        contentDescription = "Lottie Animation",
        modifier = modifier
    )
}
