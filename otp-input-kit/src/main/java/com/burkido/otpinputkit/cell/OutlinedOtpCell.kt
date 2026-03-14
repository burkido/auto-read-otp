package com.burkido.otpinputkit.cell

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.burkido.otpinputkit.OtpInputDefaults
import com.burkido.otpinputkit.style.OtpAnimationSpec
import com.burkido.otpinputkit.style.OtpCellColors
import com.burkido.otpinputkit.style.OtpCellDimensions
import com.burkido.otpinputkit.style.OtpCellPadding
import com.burkido.otpinputkit.style.OtpCursorConfig
import com.burkido.otpinputkit.style.OtpMaskConfig
import com.burkido.otpinputkit.style.OtpTextStyles

/**
 * Outlined (circle / pill) OTP cell with transparent background.
 *
 * A variant of [DefaultOtpCell] with transparent backgrounds and
 * a circular shape by default. Ideal for modern, minimal OTP designs.
 *
 * Use this inside the `cell` lambda of [com.burkido.otpinputkit.OtpInputField]:
 * ```kotlin
 * OtpInputField(
 *     state = state,
 *     otpLength = 6,
 *     cell = { index, char, focused, error ->
 *         OutlinedOtpCell(char = char, focused = focused, isError = error)
 *     }
 * )
 * ```
 */
@Composable
fun OutlinedOtpCell(
    char: Char?,
    focused: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        filledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
    ),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(
        cellWidth = defaultDimensions().cellWidth,
        cellHeight = defaultDimensions().cellWidth, // Square for circle shape
    ),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = CircleShape,
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpCursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
) {
    DefaultOtpCell(
        char = char,
        focused = focused,
        isError = isError,
        modifier = modifier,
        placeholder = placeholder,
        colors = colors,
        dimensions = dimensions,
        animationSpec = animationSpec,
        shape = shape,
        maskConfig = maskConfig,
        cursorConfig = cursorConfig,
        textStyles = textStyles,
        contentPadding = contentPadding,
    )
}

/**
 * Internal helper to avoid calling the composable function in a default argument.
 */
private fun defaultDimensions(): OtpCellDimensions = OtpInputDefaults.dimensions()
