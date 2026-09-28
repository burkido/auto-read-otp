package com.burkido.otp.timer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Default values for [CountdownTimer]. Pass only what you want to change:
 *
 * ```kotlin
 * CountdownTimerDefaults.style(backgroundColor = MaterialTheme.colorScheme.errorContainer)
 * ```
 */
public object CountdownTimerDefaults {

    /**
     * Colors taken from the current [MaterialTheme].
     *
     * @param textSize Shortcut for changing only the font size of [textStyle].
     */
    @Composable
    public fun style(
        backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
        textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        textSize: TextUnit = TextUnit.Unspecified,
        separatorColor: Color = MaterialTheme.colorScheme.onSurface,
    ): CountdownTimerStyle = CountdownTimerStyle(
        backgroundColor = backgroundColor,
        textStyle = if (textSize == TextUnit.Unspecified) textStyle else textStyle.copy(fontSize = textSize),
        separatorColor = separatorColor,
    )

    public fun dimensions(
        verticalPadding: Dp = CountdownTimerTokens.VerticalPadding,
        horizontalPadding: Dp = CountdownTimerTokens.HorizontalPadding,
        boxSpacing: Dp = CountdownTimerTokens.BoxSpacing,
    ): CountdownTimerDimensions = CountdownTimerDimensions(
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
        boxSpacing = boxSpacing,
    )
}
