package com.burkido.otp.sample.gallery

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.ui.OtpCellState
import com.burkido.otp.ui.OtpInputDefaults
import com.burkido.otp.ui.OtpInputField

private val Desk = Color(0xFFEDE8DF)
private val KeyFace = Color(0xFFFFFDF8)
private val KeyEdge = Color(0xFFCFC7B8)
private val Ink = Color(0xFF2B2A28)
private val Signal = Color(0xFFFF6B35)
private val KeyDepth = 6.dp
private val KeyShape = RoundedCornerShape(10.dp)

@Composable
internal fun KeycapsDesign(modifier: Modifier = Modifier) {
    val code = rememberTextFieldState()

    DesignFrame(background = SolidColor(Desk), modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Pair your keyboard", style = MaterialTheme.typography.titleLarge, color = Ink)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Type the code shown on the device",
                style = MaterialTheme.typography.bodyMedium,
                color = Ink.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(24.dp))
            FittedCells(count = 6, spacing = 8.dp, preferred = 46.dp) { cellWidth ->
                OtpInputField(
                    state = code,
                    otpLength = 6,
                    dimensions = OtpInputDefaults.dimensions(spacing = 8.dp),
                ) { cell ->
                    Keycap(cell = cell, width = cellWidth)
                }
            }
        }
    }
}

@Composable
private fun Keycap(cell: OtpCellState, width: Dp, modifier: Modifier = Modifier) {
    val press = remember { Animatable(0f) }
    val outline by animateColorAsState(
        targetValue = when {
            cell.isError -> Color(0xFFE5484D)
            cell.isFocused -> Signal
            else -> Color.Transparent
        },
        label = "keycap_outline",
    )

    LaunchedEffect(cell.char) {
        if (cell.char != null) {
            press.snapTo(1f)
            press.animateTo(0f, spring(dampingRatio = 0.35f, stiffness = 500f))
        }
    }

    Box(modifier = modifier.size(width = width, height = 58.dp)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = KeyDepth)
                .background(KeyEdge, KeyShape),
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp - KeyDepth)
                .offset { IntOffset(0, (KeyDepth * press.value).roundToPx()) }
                .background(KeyFace, KeyShape)
                .border(2.dp, outline, KeyShape),
        ) {
            Text(
                text = cell.char?.toString().orEmpty(),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Ink,
            )
        }
    }
}
