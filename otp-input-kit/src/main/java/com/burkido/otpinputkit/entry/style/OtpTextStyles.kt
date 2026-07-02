package com.burkido.otpinputkit.entry.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * State-aware text styles for an OTP cell.
 *
 * Allows distinct typography for filled cells (a character is present) vs.
 * unfocused empty cells that show a placeholder.
 *
 * @param filledTextStyle Style applied to the entered character.
 * @param placeholderTextStyle Style applied to the placeholder text.
 */
@Immutable
public data class OtpTextStyles(
    public val filledTextStyle: TextStyle,
    public val placeholderTextStyle: TextStyle,
)
