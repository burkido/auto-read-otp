package com.burkido.otpinputkit.entry.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Defines the sizing dimensions for an OTP input cell.
 *
 * Use [com.burkido.otpinputkit.OtpInputDefaults.dimensions] to create an instance
 * with sensible defaults, then override only the dimensions you want to change.
 */
@Immutable
public data class OtpCellDimensions(
    public val cellWidth: Dp,
    public val cellHeight: Dp,
    public val borderWidth: Dp,
    public val focusedBorderWidth: Dp,
    public val cornerRadius: Dp,
    public val spacing: Dp,
    public val textSize: TextUnit,
)
