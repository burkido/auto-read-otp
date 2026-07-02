package com.burkido.otpinputkit.entry.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Inner padding applied inside each OTP cell box.
 *
 * @param horizontal Padding on the left and right edges of the cell content.
 * @param vertical Padding on the top and bottom edges of the cell content.
 */
@Immutable
public data class OtpCellPadding(
    public val horizontal: Dp,
    public val vertical: Dp,
)
