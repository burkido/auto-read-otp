package com.burkido.otp.input

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Defines the sizing dimensions for an OTP input cell.
 *
 * Use [OtpInputDefaults.dimensions] to create an instance
 * with sensible defaults, then override only the dimensions you want to change.
 */
@Immutable
data class OtpCellDimensions(
    val cellWidth: Dp,
    val cellHeight: Dp,
    val borderWidth: Dp,
    val focusedBorderWidth: Dp,
    val cornerRadius: Dp,
    val spacing: Dp,
    val textSize: TextUnit,
)
