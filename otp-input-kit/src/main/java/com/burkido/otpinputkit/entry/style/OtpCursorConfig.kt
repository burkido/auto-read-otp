package com.burkido.otpinputkit.entry.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Configuration for the blinking cursor shown in focused, empty OTP cells.
 *
 * @param color Color of the cursor bar.
 * @param width Width of the cursor bar.
 * @param height Height of the cursor bar.
 * @param cornerRadius Corner radius of the cursor bar shape.
 * @param blinkInterval Duration in milliseconds of one blink half-cycle.
 */
@Immutable
public data class OtpCursorConfig(
    public val color: Color,
    public val width: Dp = OtpTokens.CursorWidth,
    public val height: Dp = OtpTokens.CursorHeight,
    public val cornerRadius: Dp = OtpTokens.CursorCornerRadius,
    public val blinkInterval: Long = OtpTokens.CursorBlinkInterval,
)
