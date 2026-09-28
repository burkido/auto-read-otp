package com.burkido.otp.input

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * The blinking bar shown in the focused cell while it's empty.
 * Get one from [OtpInputDefaults.cursorConfig].
 *
 * @property blinkInterval Time for the cursor to fade out or back in, in milliseconds.
 */
@Immutable
public class OtpCursorConfig(
    public val color: Color,
    public val width: Dp,
    public val height: Dp,
    public val cornerRadius: Dp,
    public val blinkInterval: Long,
) {

    public fun copy(
        color: Color = this.color,
        width: Dp = this.width,
        height: Dp = this.height,
        cornerRadius: Dp = this.cornerRadius,
        blinkInterval: Long = this.blinkInterval,
    ): OtpCursorConfig = OtpCursorConfig(
        color = color,
        width = width,
        height = height,
        cornerRadius = cornerRadius,
        blinkInterval = blinkInterval,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpCursorConfig) return false
        return color == other.color &&
            width == other.width &&
            height == other.height &&
            cornerRadius == other.cornerRadius &&
            blinkInterval == other.blinkInterval
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + cornerRadius.hashCode()
        result = 31 * result + blinkInterval.hashCode()
        return result
    }
}
