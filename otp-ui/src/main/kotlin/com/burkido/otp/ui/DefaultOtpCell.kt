package com.burkido.otp.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign

/**
 * The rounded box cell [OtpInputField] uses when you don't pass your own `cell`.
 *
 * Call it from a custom `cell` slot when you only want to change a few values for some cells:
 *
 * ```kotlin
 * OtpInputField(state = otpState, otpLength = 6) { cell ->
 *     DefaultOtpCell(state = cell, shape = if (cell.index == 0) CircleShape else RoundedCornerShape(8.dp))
 * }
 * ```
 */
@Composable
public fun DefaultOtpCell(
    state: OtpCellState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = OtpInputDefaults.shape(dimensions.cornerRadius),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpInputDefaults.cursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
) {
    val borderColor by animateColorAsState(
        targetValue = colors.borderColor(state.isFocused, state.isFilled, state.isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_border_color",
    )
    val containerColor by animateColorAsState(
        targetValue = colors.containerColor(state.isFocused, state.isFilled, state.isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_container_color",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (state.isFocused) dimensions.focusedBorderWidth else dimensions.borderWidth,
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_border_width",
    )

    val shake = rememberErrorShake(state.isError, animationSpec)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .horizontalOffset(shake)
            .disabledAlpha(state.isEnabled)
            .size(dimensions.cellWidth, dimensions.cellHeight)
            .border(borderWidth, borderColor, shape)
            .clip(shape)
            .background(containerColor)
            .padding(horizontal = contentPadding.horizontal, vertical = contentPadding.vertical),
    ) {
        CellContent(state, placeholder, colors, animationSpec, maskConfig, cursorConfig, textStyles)
    }
}

/** Shared by the built-in cells: the character, the cursor, or the placeholder. */
@Composable
internal fun CellContent(
    state: OtpCellState,
    placeholder: String,
    colors: OtpCellColors,
    animationSpec: OtpAnimationSpec,
    maskConfig: OtpMaskConfig,
    cursorConfig: OtpCursorConfig,
    textStyles: OtpTextStyles,
) {
    val displayText = rememberDisplayText(state.char, maskConfig)
    when {
        state.isFilled -> AnimatedContent(
            targetState = displayText,
            transitionSpec = { enterTransitionFor(animationSpec) togetherWith ExitTransition.None },
            label = "otp_cell_char",
        ) { text ->
            Text(
                text = text,
                style = textStyles.filledTextStyle,
                color = colors.textColor,
                textAlign = TextAlign.Center,
            )
        }

        state.isFocused -> CursorIndicator(config = cursorConfig)

        placeholder.isNotEmpty() -> Text(
            text = placeholder,
            style = textStyles.placeholderTextStyle,
            color = colors.placeholderColor,
            textAlign = TextAlign.Center,
        )
    }
}
