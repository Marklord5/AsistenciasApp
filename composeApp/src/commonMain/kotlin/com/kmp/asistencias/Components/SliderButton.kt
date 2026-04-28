package com.kmp.asistencias.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Themes.BlueDeep
import com.kmp.asistencias.Themes.White
import com.kmp.asistencias.Themes.PrimaryGradient
import kotlin.math.roundToInt

/* BOTON DESLIZABLE DE HOME */
@Composable
fun SlideToActButton(
    text: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var trackWidthPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    val trackHeight = 84.dp
    val thumbSize = 64.dp
    val padding = 10.dp

    val thumbSizePx = with(density) { thumbSize.toPx() }
    val paddingPx = with(density) { padding.toPx() }

    val maxOffsetPx = remember(trackWidthPx) {
        (trackWidthPx - thumbSizePx - (paddingPx * 2)).coerceAtLeast(0f)
    }

    val dragState = rememberDraggableState { delta ->
        offsetX = (offsetX + delta).coerceIn(0f, maxOffsetPx)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(trackHeight)
            .onGloballyPositioned { coordinates ->
                trackWidthPx = coordinates.size.width
            }
            .clip(RoundedCornerShape(trackHeight / 2))
            .background(PrimaryGradient)
            .padding(padding),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text.uppercase(),
                color = White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 55.dp)

            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = White.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(thumbSize)
                .clip(CircleShape)
                .background(White)
                .draggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        val confirmLimit = maxOffsetPx * 0.85f

                        if (offsetX >= confirmLimit) {
                            offsetX = maxOffsetPx
                            onConfirm()
                        }

                        offsetX = 0f
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = BlueDeep,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}