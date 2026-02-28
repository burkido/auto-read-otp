package com.burkido.otpinputkit

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.burkido.otpinputkit.style.CellAnimation
import com.burkido.otpinputkit.style.OtpAnimationSpec
import com.burkido.otpinputkit.style.OtpCellColors
import com.burkido.otpinputkit.style.OtpCellDimensions

/**
 * Contains default values and factory functions for OTP input components.
 *
 * Follows the Material3 `*Defaults` pattern (e.g. `TextFieldDefaults`),
 * so developers can call `OtpInputDefaults.colors(focusedBorderColor = ...)` to
 * override only specific values while keeping sensible defaults.
 */
object OtpInputDefaults {

    /**
     * Creates an [OtpCellColors] instance with Material3 defaults.
     * Override individual colors as needed.
     */
    @Composable
    fun colors(
        focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor: Color = MaterialTheme.colorScheme.outline,
        errorBorderColor: Color = MaterialTheme.colorScheme.error,
        filledBorderColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        focusedContainerColor: Color = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor: Color = MaterialTheme.colorScheme.surface,
        errorContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
        filledContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        textColor: Color = MaterialTheme.colorScheme.onSurface,
        cursorColor: Color = MaterialTheme.colorScheme.primary,
        placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
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

    /**
     * Creates an [OtpCellDimensions] instance with sensible defaults.
     */
    fun dimensions(
        cellWidth: Dp = 48.dp,
        cellHeight: Dp = 56.dp,
        borderWidth: Dp = 1.5.dp,
        focusedBorderWidth: Dp = 2.dp,
        cornerRadius: Dp = 12.dp,
        spacing: Dp = 8.dp,
        textSize: TextUnit = 24.sp,
    ): OtpCellDimensions = OtpCellDimensions(
        cellWidth = cellWidth,
        cellHeight = cellHeight,
        borderWidth = borderWidth,
        focusedBorderWidth = focusedBorderWidth,
        cornerRadius = cornerRadius,
        spacing = spacing,
        textSize = textSize,
    )

    /**
     * Creates an [OtpAnimationSpec] instance with sensible defaults.
     */
    fun animationSpec(
        cursorBlinkInterval: Long = 500L,
        shakeOnError: Boolean = true,
        cellEntryAnimation: CellAnimation = CellAnimation.Scale,
        animationDuration: Int = 150,
    ): OtpAnimationSpec = OtpAnimationSpec(
        cursorBlinkInterval = cursorBlinkInterval,
        shakeOnError = shakeOnError,
        cellEntryAnimation = cellEntryAnimation,
        animationDuration = animationDuration,
    )

    /**
     * Default shape for OTP cells.
     */
    fun shape(cornerRadius: Dp = 12.dp): Shape = RoundedCornerShape(cornerRadius)

    /**
     * Default text style for OTP cell characters.
     */
    fun textStyle(textSize: TextUnit = 24.sp): TextStyle = TextStyle(fontSize = textSize)
}
