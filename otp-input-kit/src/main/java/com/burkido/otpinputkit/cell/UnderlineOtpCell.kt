package com.burkido.otpinputkit.cell

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.burkido.otpinputkit.OtpInputDefaults
import com.burkido.otpinputkit.style.OtpAnimationSpec
import com.burkido.otpinputkit.style.OtpCellColors
import com.burkido.otpinputkit.style.OtpCellDimensions

/**
 * Underline-style OTP cell — a digit displayed above an animated underline.
 *
 * Use this inside the `cell` lambda of [com.burkido.otpinputkit.OtpInputField]:
 * ```kotlin
 * OtpInputField(
 *     state = state,
 *     otpLength = 6,
 *     cell = { index, char, focused, error ->
 *         UnderlineOtpCell(char = char, focused = focused, isError = error)
 *     }
 * )
 * ```
 */
@Composable
fun UnderlineOtpCell(
    char: Char?,
    focused: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    obscureText: Boolean = false,
    obscureCharacter: String = "●",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    textStyle: TextStyle = OtpInputDefaults.textStyle(dimensions.textSize),
) {
    val lineColor by animateColorAsState(
        targetValue = colors.borderColor(focused, char != null, isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "underline_color",
    )
    val lineHeight by animateDpAsState(
        targetValue = if (focused) dimensions.focusedBorderWidth else dimensions.borderWidth,
        animationSpec = tween(animationSpec.animationDuration),
        label = "underline_height",
    )

    val displayText = when {
        char != null && obscureText -> obscureCharacter
        char != null -> char.toString()
        else -> ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(dimensions.cellWidth),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
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
            }
        }

        // Underline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(lineHeight)
                .background(lineColor),
        )
    }
}
