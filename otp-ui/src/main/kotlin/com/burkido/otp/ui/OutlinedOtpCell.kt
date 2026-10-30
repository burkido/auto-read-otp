package com.burkido.otp.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * A round cell with a transparent background. It's [DefaultOtpCell] with different defaults.
 *
 * ```kotlin
 * OtpInputField(state = otpState, otpLength = 6) { cell ->
 *     OutlinedOtpCell(state = cell)
 * }
 * ```
 */
@Composable
public fun OutlinedOtpCell(
    state: OtpCellState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        filledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
    ),
    // Square by default so CircleShape draws a circle rather than an oval.
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(cellHeight = OtpTokens.CellWidth),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = CircleShape,
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpInputDefaults.cursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
) {
    DefaultOtpCell(
        state = state,
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
