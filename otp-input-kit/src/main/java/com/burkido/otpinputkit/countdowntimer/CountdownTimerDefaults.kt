package com.burkido.otpinputkit.countdowntimer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerSize
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerStyle
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerTokens

/**
 * Contains default values and factory functions for [CountdownTimer].
 *
 * Follows the Material3 `*Defaults` pattern (e.g. `ButtonDefaults`).
 * Override only the values you need while keeping sensible defaults:
 *
 * ```kotlin
 * CountdownTimerDefaults.style(
 *     backgroundColor = Color.Red.copy(alpha = 0.1f)
 * )
 * ```
 */
public object CountdownTimerDefaults {

    /**
     * Creates a [CountdownTimerStyle] with Material3-derived defaults.
     *
     * @param backgroundColor Background color of each time box.
     *   Defaults to [MaterialTheme.colorScheme.primaryContainer].
     * @param textStyle Text style applied to time-unit values.
     *   Defaults to a bold variant of [MaterialTheme.typography.labelLarge]
     *   at the given [textSize].
     * @param textSize Font size override for the time-unit text.
     *   Ignored when [textStyle] is provided explicitly.
     * @param separatorColor Color of the colon `:` separators.
     *   Defaults to [MaterialTheme.colorScheme.onSurface].
     */
    @Composable
    public fun style(
        backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primaryContainer,
        textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        textSize: TextUnit = TextUnit.Unspecified,
        separatorColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    ): CountdownTimerStyle {
        val resolvedTextStyle = if (textSize != TextUnit.Unspecified) {
            textStyle.copy(fontSize = textSize)
        } else {
            textStyle
        }
        return CountdownTimerStyle(
            backgroundColor = backgroundColor,
            textStyle = resolvedTextStyle,
            separatorColor = separatorColor,
        )
    }

    /**
     * Creates a [CountdownTimerSize] with sensible defaults.
     *
     * Override only the values you need:
     * ```kotlin
     * CountdownTimerDefaults.size(textSize = 18.sp)
     * ```
     *
     * @param verticalPadding Vertical padding inside each time box.
     * @param horizontalPadding Horizontal padding inside each time box.
     * @param boxSpacing Space between a time box and its adjacent colon separator.
     * @param textSize Font size of the time-unit text.
     */
    public fun size(
        verticalPadding: Dp = CountdownTimerTokens.VerticalPadding,
        horizontalPadding: Dp = CountdownTimerTokens.HorizontalPadding,
        boxSpacing: Dp = CountdownTimerTokens.BoxSpacing,
        textSize: TextUnit = CountdownTimerTokens.TextSize,
    ): CountdownTimerSize = CountdownTimerSize(
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
        boxSpacing = boxSpacing,
        textSize = textSize,
    )
}
