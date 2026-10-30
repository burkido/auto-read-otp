package com.burkido.otp.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * A cell drawn as a character above a line that changes color and thickness with the cell's state.
 * The container colors from [colors] are not used.
 *
 * ```kotlin
 * OtpInputField(state = otpState, otpLength = 6) { cell ->
 *     UnderlineOtpCell(state = cell)
 * }
 * ```
 */
@Composable
public fun UnderlineOtpCell(
    state: OtpCellState,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpInputDefaults.cursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
) {
    val lineColor by animateColorAsState(
        targetValue = colors.borderColor(state.isFocused, state.isFilled, state.isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_underline_color",
    )
    val lineThickness by animateDpAsState(
        targetValue = if (state.isFocused) dimensions.focusedBorderWidth else dimensions.borderWidth,
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_underline_thickness",
    )

    val shake = rememberErrorShake(state.isError, animationSpec)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .horizontalOffset(shake)
            .disabledAlpha(state.isEnabled)
            .size(dimensions.cellWidth, dimensions.cellHeight),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = contentPadding.horizontal, vertical = contentPadding.vertical),
        ) {
            CellContent(state, placeholder, colors, animationSpec, maskConfig, cursorConfig, textStyles)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(lineThickness)
                .background(lineColor),
        )
    }
}
