package com.burkido.otp.input

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
    const val CursorBlinkInterval = 500L

    const val AnimationDuration = 150
    val ShakeAmplitude = 8.dp
    const val ShakeStepDuration = 50
    const val ShakeRepeatCount = 3

    const val MaskCharacter = '●'
    const val MaskDelay = 300L
}
