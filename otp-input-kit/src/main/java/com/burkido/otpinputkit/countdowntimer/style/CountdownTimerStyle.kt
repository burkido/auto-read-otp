package com.burkido.otpinputkit.countdowntimer.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Visual styling for [com.burkido.otpinputkit.countdowntimer.CountdownTimer].
 *
 * Create instances via [com.burkido.otpinputkit.countdowntimer.CountdownTimerDefaults.style].
 *
 * @param backgroundColor Background color of each time box.
 * @param textStyle Text style applied to each time-unit value (hours, minutes, seconds).
 * @param separatorColor Color of the colon `:` separators between boxes.
 */
@Immutable
data class CountdownTimerStyle(
    val backgroundColor: Color,
    val textStyle: TextStyle,
    val separatorColor: Color,
)
