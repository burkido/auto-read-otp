package com.burkido.otpinputkit.entry.cell

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.burkido.otpinputkit.entry.style.OtpCursorConfig

/**
 * Internal blinking cursor indicator for focused OTP cells.
 */
@Composable
internal fun CursorIndicator(
    config: OtpCursorConfig,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "cursor_blink")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = config.blinkInterval.toInt()),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "cursor_alpha",
    )

    Box(
        modifier = modifier
            .width(config.width)
            .height(config.height)
            .alpha(alpha)
            .background(config.color, RoundedCornerShape(config.cornerRadius)),
    )
}
