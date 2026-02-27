package com.burkido.otpinputkit.cell

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.burkido.otpinputkit.OtpInputDefaults
import com.burkido.otpinputkit.style.OtpAnimationSpec
import com.burkido.otpinputkit.style.OtpCellColors
import com.burkido.otpinputkit.style.OtpCellDimensions

/**
 * Default rounded-box OTP cell with animated border and background.
 *
 * This is the cell used by [com.burkido.otpinputkit.OtpInputField] when no
 * custom `cell` lambda is provided. You can also use it explicitly inside a
 * custom `cell` lambda to compose default behavior with your own additions.
 *
 * @param char The character to display, or `null` if the cell is empty.
 * @param focused Whether this cell is currently focused.
 * @param isError Whether the input is in an error state.
 * @param modifier Modifier for the root element.
 * @param obscureText If true, the character is replaced with [obscureCharacter].
 * @param obscureCharacter The character shown when [obscureText] is true.
 * @param placeholder Text shown in unfocused empty cells.
 * @param colors Color configuration — defaults to [OtpInputDefaults.colors].
 * @param dimensions Size configuration — defaults to [OtpInputDefaults.dimensions].
 * @param animationSpec Animation configuration.
 * @param shape Shape of the cell.
 * @param textStyle Text style for the character.
 */
@Composable
fun DefaultOtpCell(
    char: Char?,
    focused: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    obscureText: Boolean = false,
    obscureCharacter: String = "●",
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = RoundedCornerShape(dimensions.cornerRadius),
    textStyle: TextStyle = OtpInputDefaults.textStyle(dimensions.textSize),
) {
    val borderColor by animateColorAsState(
        targetValue = colors.borderColor(focused, char != null, isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_border_color",
    )
    val containerColor by animateColorAsState(
        targetValue = colors.containerColor(focused, char != null, isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_container_color",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (focused) dimensions.focusedBorderWidth else dimensions.borderWidth,
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_border_width",
    )

    val displayText = when {
        char != null && obscureText -> obscureCharacter
        char != null -> char.toString()
        else -> ""
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensions.cellWidth, dimensions.cellHeight)
            .border(borderWidth, borderColor, shape)
            .clip(shape)
            .background(containerColor),
    ) {
        when {
            char != null -> {
                Text(
                    text = displayText,
                    style = textStyle,
                    color = colors.textColor,
                    textAlign = TextAlign.Center,
                )
            }
            focused -> {
                CursorIndicator(
                    cursorColor = colors.cursorColor,
                    blinkInterval = animationSpec.cursorBlinkInterval,
                )
            }
            placeholder.isNotEmpty() -> {
                Text(
                    text = placeholder,
                    style = textStyle,
                    color = colors.placeholderColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
