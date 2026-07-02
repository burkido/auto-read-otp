package com.burkido.otpinputkit.entry.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Defines the colors used by an OTP input cell in various states.
 *
 * Use [com.burkido.otpinputkit.OtpInputDefaults.colors] to create an instance
 * with Material3 defaults, then override only the colors you want to change.
 */
@Immutable
public data class OtpCellColors(
    public val focusedBorderColor: Color,
    public val unfocusedBorderColor: Color,
    public val errorBorderColor: Color,
    public val filledBorderColor: Color,
    public val focusedContainerColor: Color,
    public val unfocusedContainerColor: Color,
    public val errorContainerColor: Color,
    public val filledContainerColor: Color,
    public val textColor: Color,
    public val cursorColor: Color,
    public val placeholderColor: Color,
) {
    /**
     * Resolves the border color based on the current cell state.
     */
    public fun borderColor(focused: Boolean, filled: Boolean, error: Boolean): Color = when {
        error -> errorBorderColor
        focused -> focusedBorderColor
        filled -> filledBorderColor
        else -> unfocusedBorderColor
    }

    /**
     * Resolves the container (background) color based on the current cell state.
     */
    public fun containerColor(focused: Boolean, filled: Boolean, error: Boolean): Color = when {
        error -> errorContainerColor
        focused -> focusedContainerColor
        filled -> filledContainerColor
        else -> unfocusedContainerColor
    }
}
