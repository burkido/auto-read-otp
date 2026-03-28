package com.burkido.otpinputkit.entry

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.burkido.otpinputkit.entry.style.CellAnimation
import com.burkido.otpinputkit.entry.style.OtpAnimationSpec
import com.burkido.otpinputkit.entry.style.OtpCellColors
import com.burkido.otpinputkit.entry.style.OtpCellDimensions
import com.burkido.otpinputkit.entry.style.OtpCellPadding
import com.burkido.otpinputkit.entry.style.OtpCursorConfig
import com.burkido.otpinputkit.entry.style.OtpMaskConfig
import com.burkido.otpinputkit.entry.style.OtpTextStyles
import com.burkido.otpinputkit.entry.style.OtpTokens

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

    /**
     * Creates an [OtpAnimationSpec] instance with sensible defaults.
     */
    fun animationSpec(
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

    /**
     * Creates an [OtpMaskConfig] for obscuring entered digits.
     *
     * When [isMasked] is true each digit is shown for [maskDelay] ms before
     * being replaced by [maskCharacter].
     */
    fun maskConfig(
        isMasked: Boolean = false,
        maskCharacter: Char = OtpTokens.MaskCharacter,
        maskDelay: Long = OtpTokens.MaskDelay,
    ): OtpMaskConfig = OtpMaskConfig(
        isMasked = isMasked,
        maskCharacter = maskCharacter,
        maskDelay = maskDelay,
    )

    /**
     * Creates an [OtpCursorConfig] with Material3-derived defaults.
     *
     * The cursor [color] defaults to the primary color of the current theme.
     */
    @Composable
    fun cursorConfig(
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

    /**
     * Creates an [OtpCellPadding] for inner padding inside each cell box.
     */
    fun contentPadding(
        horizontal: Dp = OtpTokens.ContentPaddingHorizontal,
        vertical: Dp = OtpTokens.ContentPaddingVertical,
    ): OtpCellPadding = OtpCellPadding(
        horizontal = horizontal,
        vertical = vertical,
    )

    /**
     * Creates an [OtpTextStyles] with state-aware text styles.
     *
     * [filledTextStyle] is applied to an entered character; [placeholderTextStyle]
     * is applied to placeholder text in unfocused empty cells.
     */
    @Composable
    fun textStyles(
        textSize: TextUnit = OtpTokens.TextSize,
        filledTextStyle: TextStyle = TextStyle(
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
        ),
        placeholderTextStyle: TextStyle = TextStyle(
            fontSize = textSize,
            fontWeight = FontWeight.Light,
        ),
    ): OtpTextStyles = OtpTextStyles(
        filledTextStyle = filledTextStyle,
        placeholderTextStyle = placeholderTextStyle,
    )

    /**
     * Default shape for OTP cells.
     */
    fun shape(cornerRadius: Dp = OtpTokens.CornerRadius): Shape = RoundedCornerShape(cornerRadius)
}
