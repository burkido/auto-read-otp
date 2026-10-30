package com.burkido.otp.ui

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Raw default values. Consumers change them through [OtpInputDefaults]. */
internal object OtpTokens {
    val CellWidth = 48.dp
    val CellHeight = 56.dp
    val BorderWidth = 1.5.dp
    val FocusedBorderWidth = 2.dp
    val CornerRadius = 12.dp
    val Spacing = 8.dp
    val TextSize = 24.sp

    val ContentPaddingHorizontal = 8.dp
    val ContentPaddingVertical = 12.dp

    val CursorWidth = 2.dp
    val CursorHeight = 24.dp
    val CursorCornerRadius = 1.dp
    const val CURSOR_BLINK_INTERVAL = 500L

    const val ANIMATION_DURATION = 150
    val ShakeAmplitude = 8.dp
    const val SHAKE_STEP_DURATION = 50
    const val SHAKE_REPEAT_COUNT = 3

    const val MASK_CHARACTER = '●'
    const val MASK_DELAY = 300L
}
