package com.burkido.otpinputkit.cell

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.burkido.otpinputkit.OtpInputDefaults
import com.burkido.otpinputkit.style.OtpAnimationSpec
import com.burkido.otpinputkit.style.OtpCellColors
import com.burkido.otpinputkit.style.OtpCellDimensions

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
    obscureText: Boolean = false,
    obscureCharacter: String = "●",
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        filledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
    ),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(
        cellWidth = dimensions().cellWidth,
        cellHeight = dimensions().cellWidth, // Square for circle shape
    ),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = CircleShape,
    textStyle: TextStyle = OtpInputDefaults.textStyle(dimensions.textSize),
) {
    DefaultOtpCell(
        char = char,
        focused = focused,
        isError = isError,
        modifier = modifier,
        obscureText = obscureText,
        obscureCharacter = obscureCharacter,
        placeholder = placeholder,
        colors = colors,
        dimensions = dimensions,
        animationSpec = animationSpec,
        shape = shape,
        textStyle = textStyle,
    )
}

/**
 * Internal helper to avoid calling the composable function in a default argument.
 */
private fun dimensions(): OtpCellDimensions = OtpInputDefaults.dimensions()
