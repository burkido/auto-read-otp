package com.burkido.otp.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/**
 * Text styles for a cell holding a character and for an empty cell showing the placeholder.
 * Get one from [OtpInputDefaults.textStyles].
 */
@Immutable
public class OtpTextStyles(public val filledTextStyle: TextStyle, public val placeholderTextStyle: TextStyle) {

    public fun copy(
        filledTextStyle: TextStyle = this.filledTextStyle,
        placeholderTextStyle: TextStyle = this.placeholderTextStyle,
    ): OtpTextStyles = OtpTextStyles(filledTextStyle = filledTextStyle, placeholderTextStyle = placeholderTextStyle)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpTextStyles) return false
        return filledTextStyle == other.filledTextStyle && placeholderTextStyle == other.placeholderTextStyle
    }

    override fun hashCode(): Int = 31 * filledTextStyle.hashCode() + placeholderTextStyle.hashCode()
}
