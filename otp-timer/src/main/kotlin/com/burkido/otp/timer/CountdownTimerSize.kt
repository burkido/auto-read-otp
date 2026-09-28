package com.burkido.otp.timer

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Size configuration for [CountdownTimer].
 *
 * Create instances via [CountdownTimerDefaults.size],
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
data class CountdownTimerSize(
    val verticalPadding: Dp,
    val horizontalPadding: Dp,
    val boxSpacing: Dp,
    val textSize: TextUnit,
)
