package com.burkido.otp.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Colors for an OTP cell in each of its states.
 *
 * Get one from [OtpInputDefaults.colors] and override only what you need. When several states
 * apply at once, error wins over focused, and focused wins over filled.
 */
@Immutable
public class OtpCellColors(
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

    public fun copy(
        focusedBorderColor: Color = this.focusedBorderColor,
        unfocusedBorderColor: Color = this.unfocusedBorderColor,
        errorBorderColor: Color = this.errorBorderColor,
        filledBorderColor: Color = this.filledBorderColor,
        focusedContainerColor: Color = this.focusedContainerColor,
        unfocusedContainerColor: Color = this.unfocusedContainerColor,
        errorContainerColor: Color = this.errorContainerColor,
        filledContainerColor: Color = this.filledContainerColor,
        textColor: Color = this.textColor,
        cursorColor: Color = this.cursorColor,
        placeholderColor: Color = this.placeholderColor,
    ): OtpCellColors = OtpCellColors(
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        errorBorderColor = errorBorderColor,
        filledBorderColor = filledBorderColor,
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        errorContainerColor = errorContainerColor,
        filledContainerColor = filledContainerColor,
        textColor = textColor,
        cursorColor = cursorColor,
        placeholderColor = placeholderColor,
    )

    internal fun borderColor(focused: Boolean, filled: Boolean, error: Boolean): Color = when {
        error -> errorBorderColor
        focused -> focusedBorderColor
        filled -> filledBorderColor
        else -> unfocusedBorderColor
    }

    internal fun containerColor(focused: Boolean, filled: Boolean, error: Boolean): Color = when {
        error -> errorContainerColor
        focused -> focusedContainerColor
        filled -> filledContainerColor
        else -> unfocusedContainerColor
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpCellColors) return false
        return focusedBorderColor == other.focusedBorderColor &&
            unfocusedBorderColor == other.unfocusedBorderColor &&
            errorBorderColor == other.errorBorderColor &&
            filledBorderColor == other.filledBorderColor &&
            focusedContainerColor == other.focusedContainerColor &&
            unfocusedContainerColor == other.unfocusedContainerColor &&
            errorContainerColor == other.errorContainerColor &&
            filledContainerColor == other.filledContainerColor &&
            textColor == other.textColor &&
            cursorColor == other.cursorColor &&
            placeholderColor == other.placeholderColor
    }

    override fun hashCode(): Int {
        var result = focusedBorderColor.hashCode()
        result = 31 * result + unfocusedBorderColor.hashCode()
        result = 31 * result + errorBorderColor.hashCode()
        result = 31 * result + filledBorderColor.hashCode()
        result = 31 * result + focusedContainerColor.hashCode()
        result = 31 * result + unfocusedContainerColor.hashCode()
        result = 31 * result + errorContainerColor.hashCode()
        result = 31 * result + filledContainerColor.hashCode()
        result = 31 * result + textColor.hashCode()
        result = 31 * result + cursorColor.hashCode()
        result = 31 * result + placeholderColor.hashCode()
        return result
    }
}
