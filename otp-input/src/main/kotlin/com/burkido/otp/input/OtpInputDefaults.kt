package com.burkido.otp.input

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Default values for [OtpInputField] and the built-in cells.
 *
 * Each function takes every value as an optional argument, so you can change one thing and keep
 * the rest:
 *
 * ```kotlin
 * OtpInputDefaults.colors(focusedBorderColor = Color(0xFF00D4FF))
 * ```
 */
public object OtpInputDefaults {

    /** Colors taken from the current [MaterialTheme]. */
    @Composable
    public fun colors(
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

    public fun dimensions(
        cellWidth: Dp = OtpTokens.CellWidth,
        cellHeight: Dp = OtpTokens.CellHeight,
        borderWidth: Dp = OtpTokens.BorderWidth,
        focusedBorderWidth: Dp = OtpTokens.FocusedBorderWidth,
        cornerRadius: Dp = OtpTokens.CornerRadius,
        spacing: Dp = OtpTokens.Spacing,
        textSize: TextUnit = OtpTokens.TextSize,
    ): OtpCellDimensions = OtpCellDimensions(
        cellWidth = cellWidth,
        cellHeight = cellHeight,
        borderWidth = borderWidth,
        focusedBorderWidth = focusedBorderWidth,
        cornerRadius = cornerRadius,
        spacing = spacing,
        textSize = textSize,
    )

    public fun animationSpec(
        shakeOnError: Boolean = true,
        cellEntryAnimation: CellAnimation = CellAnimation.Scale,
        animationDuration: Int = OtpTokens.AnimationDuration,
        shakeAmplitude: Dp = OtpTokens.ShakeAmplitude,
        shakeStepDuration: Int = OtpTokens.ShakeStepDuration,
        shakeRepeatCount: Int = OtpTokens.ShakeRepeatCount,
    ): OtpAnimationSpec = OtpAnimationSpec(
        shakeOnError = shakeOnError,
        cellEntryAnimation = cellEntryAnimation,
        animationDuration = animationDuration,
        shakeAmplitude = shakeAmplitude,
        shakeStepDuration = shakeStepDuration,
        shakeRepeatCount = shakeRepeatCount,
    )

    /** Masking is off by default. Turn it on for codes that shouldn't stay visible on screen. */
    public fun maskConfig(
        isMasked: Boolean = false,
        maskCharacter: Char = OtpTokens.MaskCharacter,
        maskDelay: Long = OtpTokens.MaskDelay,
    ): OtpMaskConfig = OtpMaskConfig(
        isMasked = isMasked,
        maskCharacter = maskCharacter,
        maskDelay = maskDelay,
    )

    /** A cursor in the theme's primary color. */
    @Composable
    public fun cursorConfig(
        color: Color = MaterialTheme.colorScheme.primary,
        width: Dp = OtpTokens.CursorWidth,
        height: Dp = OtpTokens.CursorHeight,
        cornerRadius: Dp = OtpTokens.CursorCornerRadius,
        blinkInterval: Long = OtpTokens.CursorBlinkInterval,
    ): OtpCursorConfig = OtpCursorConfig(
        color = color,
        width = width,
        height = height,
        cornerRadius = cornerRadius,
        blinkInterval = blinkInterval,
    )

    public fun contentPadding(
        horizontal: Dp = OtpTokens.ContentPaddingHorizontal,
        vertical: Dp = OtpTokens.ContentPaddingVertical,
    ): OtpCellPadding = OtpCellPadding(horizontal = horizontal, vertical = vertical)

    public fun textStyles(
        textSize: TextUnit = OtpTokens.TextSize,
        filledTextStyle: TextStyle = TextStyle(fontSize = textSize, fontWeight = FontWeight.Bold),
        placeholderTextStyle: TextStyle = TextStyle(fontSize = textSize, fontWeight = FontWeight.Light),
    ): OtpTextStyles = OtpTextStyles(
        filledTextStyle = filledTextStyle,
        placeholderTextStyle = placeholderTextStyle,
    )

    public fun shape(cornerRadius: Dp = OtpTokens.CornerRadius): Shape = RoundedCornerShape(cornerRadius)

    /** A number pad with autocorrect off. */
    public val keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        autoCorrectEnabled = false,
    )
}
