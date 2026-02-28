package com.burkido.otpinputkit.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Defines the sizing dimensions for an OTP input cell.
 *
 * Use [com.burkido.otpinputkit.OtpInputDefaults.dimensions] to create an instance
 * with sensible defaults, then override only the dimensions you want to change.
 */
@Immutable
data class OtpCellDimensions(
    val cellWidth: Dp = 48.dp,
    val cellHeight: Dp = 56.dp,
    val borderWidth: Dp = 1.5.dp,
    val focusedBorderWidth: Dp = 2.dp,
    val cornerRadius: Dp = 12.dp,
    val spacing: Dp = 8.dp,
    val textSize: TextUnit = 24.sp,
)
