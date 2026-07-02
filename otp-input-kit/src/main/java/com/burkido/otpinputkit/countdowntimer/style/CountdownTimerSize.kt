package com.burkido.otpinputkit.countdowntimer.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Size configuration for [com.burkido.otpinputkit.countdowntimer.CountdownTimer].
 *
 * Create instances via [com.burkido.otpinputkit.countdowntimer.CountdownTimerDefaults.size],
 * overriding only the values you need:
 *
 * ```kotlin
 * CountdownTimerDefaults.size(textSize = 18.sp)
 * ```
 *
 * @param verticalPadding Vertical padding inside each time box.
 * @param horizontalPadding Horizontal padding inside each time box.
 * @param boxSpacing Space between a time box and its adjacent colon separator.
 * @param textSize Font size of the time-unit text.
 */
public data class CountdownTimerSize(
    public val verticalPadding: Dp,
    public val horizontalPadding: Dp,
    public val boxSpacing: Dp,
    public val textSize: TextUnit,
)
