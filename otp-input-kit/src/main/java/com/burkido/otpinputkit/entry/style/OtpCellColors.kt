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
data class OtpCellColors(
    val focusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val errorBorderColor: Color,
    val filledBorderColor: Color,
    val focusedContainerColor: Color,
    val unfocusedContainerColor: Color,
    val errorContainerColor: Color,
    val filledContainerColor: Color,
    val textColor: Color,
    val cursorColor: Color,
    val placeholderColor: Color,
) {
    /**
     * Resolves the border color based on the current cell state.
     */
    fun borderColor(focused: Boolean, filled: Boolean, error: Boolean): Color = when {
        error -> errorBorderColor
        focused -> focusedBorderColor
        filled -> filledBorderColor
        else -> unfocusedBorderColor
    }

    /**
     * Resolves the container (background) color based on the current cell state.
     */
    fun containerColor(focused: Boolean, filled: Boolean, error: Boolean): Color = when {
        error -> errorContainerColor
        focused -> focusedContainerColor
        filled -> filledContainerColor
        else -> unfocusedContainerColor
    }
}
